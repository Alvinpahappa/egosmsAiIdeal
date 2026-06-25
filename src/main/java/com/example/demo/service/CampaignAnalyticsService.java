package com.example.demo.service;

import com.example.demo.dto.DayEngagement;
import com.example.demo.dto.HourlyEngagement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Simulates EgoSMS historical campaign data from delivery logs and shortened-link click tracking.
 */
@Service
public class CampaignAnalyticsService {

    public List<HourlyEngagement> getHourlyEngagement(String campaignType) {
        return switch (normalizeType(campaignType)) {
            case "transactional" -> transactionalHourly();
            case "reminder" -> reminderHourly();
            default -> marketingHourly();
        };
    }

    public List<DayEngagement> getDayEngagement(String campaignType) {
        return switch (normalizeType(campaignType)) {
            case "transactional" -> transactionalDays();
            case "reminder" -> reminderDays();
            default -> marketingDays();
        };
    }

    public String getDataSummary(String campaignType) {
        return switch (normalizeType(campaignType)) {
            case "transactional" ->
                    "847 campaigns analyzed over 90 days (delivery reports + shortened-link clicks).";
            case "reminder" ->
                    "412 reminder campaigns analyzed over 90 days (delivery reports + shortened-link clicks).";
            default ->
                    "1,204 marketing campaigns analyzed over 90 days (delivery reports + shortened-link clicks).";
        };
    }

    private String normalizeType(String campaignType) {
        if (campaignType == null || campaignType.isBlank()) {
            return "marketing";
        }
        return campaignType.trim().toLowerCase(Locale.ROOT);
    }

    private List<HourlyEngagement> marketingHourly() {
        int[][] data = {
                {0, 1200, 18}, {1, 800, 10}, {2, 500, 6}, {3, 400, 5}, {4, 600, 8},
                {5, 1400, 22}, {6, 3200, 96}, {7, 5800, 232}, {8, 7200, 288},
                {9, 6100, 214}, {10, 5400, 162}, {11, 4900, 147}, {12, 6800, 272},
                {13, 5600, 196}, {14, 5200, 156}, {15, 4800, 134}, {16, 5100, 153},
                {17, 6200, 198}, {18, 7800, 312}, {19, 9100, 409}, {20, 8600, 344},
                {21, 6400, 224}, {22, 3800, 95}, {23, 2100, 42}
        };
        return toHourly(data);
    }

    private List<HourlyEngagement> transactionalHourly() {
        int[][] data = {
                {0, 900, 45}, {1, 700, 35}, {2, 500, 25}, {3, 400, 20}, {4, 500, 25},
                {5, 1100, 66}, {6, 4200, 294}, {7, 8900, 712}, {8, 9800, 784},
                {9, 9200, 736}, {10, 7600, 532}, {11, 6800, 476}, {12, 7100, 497},
                {13, 6900, 483}, {14, 7200, 504}, {15, 6400, 384}, {16, 5800, 348},
                {17, 6100, 366}, {18, 5400, 324}, {19, 4200, 210}, {20, 3100, 124},
                {21, 2400, 96}, {22, 1800, 54}, {23, 1200, 36}
        };
        return toHourly(data);
    }

    private List<HourlyEngagement> reminderHourly() {
        int[][] data = {
                {0, 600, 12}, {1, 500, 10}, {2, 400, 8}, {3, 350, 7}, {4, 450, 9},
                {5, 900, 27}, {6, 2800, 112}, {7, 5200, 260}, {8, 6100, 305},
                {9, 5400, 243}, {10, 4600, 184}, {11, 4200, 168}, {12, 5000, 225},
                {13, 4700, 188}, {14, 4300, 172}, {15, 4100, 164}, {16, 4400, 176},
                {17, 4900, 196}, {18, 5600, 252}, {19, 6000, 300}, {20, 5200, 208},
                {21, 3600, 108}, {22, 2200, 44}, {23, 1400, 21}
        };
        return toHourly(data);
    }

    private List<DayEngagement> marketingDays() {
        return List.of(
                day("Monday", 4.2, 168),
                day("Tuesday", 4.8, 182),
                day("Wednesday", 4.5, 176),
                day("Thursday", 4.9, 191),
                day("Friday", 5.6, 205),
                day("Saturday", 3.8, 142),
                day("Sunday", 3.1, 120)
        );
    }

    private List<DayEngagement> transactionalDays() {
        return List.of(
                day("Monday", 8.4, 98),
                day("Tuesday", 8.9, 104),
                day("Wednesday", 8.7, 101),
                day("Thursday", 9.1, 109),
                day("Friday", 8.2, 96),
                day("Saturday", 5.1, 52),
                day("Sunday", 4.4, 41)
        );
    }

    private List<DayEngagement> reminderDays() {
        return List.of(
                day("Monday", 5.1, 74),
                day("Tuesday", 5.4, 79),
                day("Wednesday", 5.2, 76),
                day("Thursday", 5.6, 82),
                day("Friday", 5.0, 71),
                day("Saturday", 3.9, 48),
                day("Sunday", 3.2, 39)
        );
    }

    private DayEngagement day(String day, double rate, long campaigns) {
        return new DayEngagement(day, rate, campaigns);
    }

    private List<HourlyEngagement> toHourly(int[][] data) {
        List<HourlyEngagement> rows = new ArrayList<>();
        for (int[] row : data) {
            int hour = row[0];
            long delivered = row[1];
            long clicked = row[2];
            double rate = delivered == 0 ? 0 : (clicked * 100.0) / delivered;
            rows.add(new HourlyEngagement(hour, formatHour(hour), delivered, clicked, round(rate)));
        }
        rows.sort(Comparator.comparingInt(HourlyEngagement::hour));
        return rows;
    }

    private String formatHour(int hour) {
        if (hour == 0) {
            return "12 AM";
        }
        if (hour < 12) {
            return hour + " AM";
        }
        if (hour == 12) {
            return "12 PM";
        }
        return (hour - 12) + " PM";
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
