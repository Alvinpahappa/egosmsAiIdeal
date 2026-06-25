package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class SendTimeRecommendation {

    private String campaignType;
    private String recommendedDay;
    private String recommendedWindow;
    private String alternativeWindow;
    private double bestClickRate;
    private double averageClickRate;
    private String upliftPercent;
    private String dataSummary;
    private String aiInsight;
    private List<HourlyEngagement> hourlyBreakdown = new ArrayList<>();
    private List<DayEngagement> dayBreakdown = new ArrayList<>();
    private List<String> tips = new ArrayList<>();

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public String getRecommendedDay() {
        return recommendedDay;
    }

    public void setRecommendedDay(String recommendedDay) {
        this.recommendedDay = recommendedDay;
    }

    public String getRecommendedWindow() {
        return recommendedWindow;
    }

    public void setRecommendedWindow(String recommendedWindow) {
        this.recommendedWindow = recommendedWindow;
    }

    public String getAlternativeWindow() {
        return alternativeWindow;
    }

    public void setAlternativeWindow(String alternativeWindow) {
        this.alternativeWindow = alternativeWindow;
    }

    public double getBestClickRate() {
        return bestClickRate;
    }

    public void setBestClickRate(double bestClickRate) {
        this.bestClickRate = bestClickRate;
    }

    public double getAverageClickRate() {
        return averageClickRate;
    }

    public void setAverageClickRate(double averageClickRate) {
        this.averageClickRate = averageClickRate;
    }

    public String getUpliftPercent() {
        return upliftPercent;
    }

    public void setUpliftPercent(String upliftPercent) {
        this.upliftPercent = upliftPercent;
    }

    public String getDataSummary() {
        return dataSummary;
    }

    public void setDataSummary(String dataSummary) {
        this.dataSummary = dataSummary;
    }

    public String getAiInsight() {
        return aiInsight;
    }

    public void setAiInsight(String aiInsight) {
        this.aiInsight = aiInsight;
    }

    public List<HourlyEngagement> getHourlyBreakdown() {
        return hourlyBreakdown;
    }

    public void setHourlyBreakdown(List<HourlyEngagement> hourlyBreakdown) {
        this.hourlyBreakdown = hourlyBreakdown;
    }

    public List<DayEngagement> getDayBreakdown() {
        return dayBreakdown;
    }

    public void setDayBreakdown(List<DayEngagement> dayBreakdown) {
        this.dayBreakdown = dayBreakdown;
    }

    public List<String> getTips() {
        return tips;
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
    }
}
