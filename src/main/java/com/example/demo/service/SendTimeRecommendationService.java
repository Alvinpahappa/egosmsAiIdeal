package com.example.demo.service;

import com.example.demo.controller.GeminiChatService;
import com.example.demo.dto.DayEngagement;
import com.example.demo.dto.HourlyEngagement;
import com.example.demo.dto.SendTimeRecommendation;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class SendTimeRecommendationService {

    private final CampaignAnalyticsService campaignAnalyticsService;
    private final GeminiChatService geminiChatService;

    public SendTimeRecommendationService(
            CampaignAnalyticsService campaignAnalyticsService,
            GeminiChatService geminiChatService
    ) {
        this.campaignAnalyticsService = campaignAnalyticsService;
        this.geminiChatService = geminiChatService;
    }

    public SendTimeRecommendation recommend(String campaignType) {
        String type = normalizeType(campaignType);
        List<HourlyEngagement> hourly = campaignAnalyticsService.getHourlyEngagement(type);
        List<DayEngagement> daily = campaignAnalyticsService.getDayEngagement(type);

        HourlyEngagement bestHour = hourly.stream()
                .max(Comparator.comparingDouble(HourlyEngagement::clickRate))
                .orElseThrow();

        HourlyEngagement secondBest = hourly.stream()
                .sorted(Comparator.comparingDouble(HourlyEngagement::clickRate).reversed())
                .skip(1)
                .findFirst()
                .orElse(bestHour);

        DayEngagement bestDay = daily.stream()
                .max(Comparator.comparingDouble(DayEngagement::clickRate))
                .orElseThrow();

        double averageClickRate = hourly.stream()
                .mapToDouble(HourlyEngagement::clickRate)
                .average()
                .orElse(0);

        double uplift = averageClickRate == 0
                ? 0
                : ((bestHour.clickRate() - averageClickRate) / averageClickRate) * 100;

        SendTimeRecommendation recommendation = new SendTimeRecommendation();
        recommendation.setCampaignType(type);
        recommendation.setRecommendedDay(bestDay.day());
        recommendation.setRecommendedWindow(formatWindow(bestHour.hour()));
        recommendation.setAlternativeWindow(formatWindow(secondBest.hour()));
        recommendation.setBestClickRate(bestHour.clickRate());
        recommendation.setAverageClickRate(round(averageClickRate));
        recommendation.setUpliftPercent("+" + Math.round(uplift) + "%");
        recommendation.setDataSummary(campaignAnalyticsService.getDataSummary(type));
        recommendation.setHourlyBreakdown(hourly);
        recommendation.setDayBreakdown(daily);
        recommendation.setTips(buildTips(type, bestDay.day(), bestHour.label()));
        recommendation.setAiInsight(generateInsight(type, recommendation, hourly, daily));
        return recommendation;
    }

    private String generateInsight(
            String type,
            SendTimeRecommendation recommendation,
            List<HourlyEngagement> hourly,
            List<DayEngagement> daily
    ) {
        String hourlySummary = hourly.stream()
                .sorted(Comparator.comparingDouble(HourlyEngagement::clickRate).reversed())
                .limit(5)
                .map(h -> h.label() + " " + h.clickRate() + "%")
                .collect(Collectors.joining(", "));

        String dailySummary = daily.stream()
                .map(d -> d.day() + " " + d.clickRate() + "%")
                .collect(Collectors.joining(", "));

        String systemPrompt = """
                You are an SMS campaign analyst for EgoSMS Uganda.
                Write 2 short sentences explaining the best send time recommendation.
                Be practical, mention shortened-link click behavior, and avoid markdown.
                """;

        String userPrompt = """
                Campaign type: %s
                Recommended: %s at %s
                Alternative: %s
                Best click rate: %.1f%% vs average %.1f%% (%s uplift)
                Top hours by click rate: %s
                Click rate by day: %s
                """.formatted(
                type,
                recommendation.getRecommendedDay(),
                recommendation.getRecommendedWindow(),
                recommendation.getAlternativeWindow(),
                recommendation.getBestClickRate(),
                recommendation.getAverageClickRate(),
                recommendation.getUpliftPercent(),
                hourlySummary,
                dailySummary
        );

        try {
            return geminiChatService.askWithPersona(systemPrompt, userPrompt);
        } catch (Exception ex) {
            return fallbackInsight(recommendation);
        }
    }

    private String fallbackInsight(SendTimeRecommendation recommendation) {
        return "Based on past delivery and shortened-link click data, "
                + recommendation.getRecommendedDay() + " around "
                + recommendation.getRecommendedWindow()
                + " gives the highest engagement for "
                + recommendation.getCampaignType()
                + " campaigns. Scheduling inside this window could improve link clicks by roughly "
                + recommendation.getUpliftPercent() + " compared with sending at off-peak hours.";
    }

    private List<String> buildTips(String type, String bestDay, String bestHour) {
        return switch (type) {
            case "transactional" -> List.of(
                    "Send payment and OTP messages in the morning when open rates peak.",
                    "Avoid late-night transactional SMS unless the event is time-critical.",
                    "Use shortened links to track confirmation clicks after delivery."
            );
            case "reminder" -> List.of(
                    "Schedule reminders one day before the event, then a same-day nudge at " + bestHour + ".",
                    "Keep reminder copy short so users act before the window closes.",
                    "Track link clicks to see which reminder timing converts best."
            );
            default -> List.of(
                    "Schedule promotional campaigns on " + bestDay + " around " + bestHour + " for best clicks.",
                    "Avoid sending bulk marketing SMS between 11 PM and 6 AM.",
                    "Use EgoSMS shortened URLs to measure which send times drive the most engagement."
            );
        };
    }

    private String formatWindow(int hour) {
        int endHour = (hour + 1) % 24;
        return formatHourLabel(hour) + " - " + formatHourLabel(endHour) + " EAT";
    }

    private String formatHourLabel(int hour) {
        return String.format(Locale.ROOT, "%02d:00", hour);
    }

    private String normalizeType(String campaignType) {
        if (campaignType == null || campaignType.isBlank()) {
            return "marketing";
        }
        return campaignType.trim().toLowerCase(Locale.ROOT);
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
