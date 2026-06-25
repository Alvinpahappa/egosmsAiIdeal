package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class SmsAnalyzeResponse {

    private String detectedTone;
    private int clarityScore;
    private int segmentCount;
    private int characterCount;
    private int estimatedSmsUnits;
    private List<String> issues = new ArrayList<>();
    private String suggestedRewrite;
    private String summary;

    public String getDetectedTone() {
        return detectedTone;
    }

    public void setDetectedTone(String detectedTone) {
        this.detectedTone = detectedTone;
    }

    public int getClarityScore() {
        return clarityScore;
    }

    public void setClarityScore(int clarityScore) {
        this.clarityScore = clarityScore;
    }

    public int getSegmentCount() {
        return segmentCount;
    }

    public void setSegmentCount(int segmentCount) {
        this.segmentCount = segmentCount;
    }

    public int getCharacterCount() {
        return characterCount;
    }

    public void setCharacterCount(int characterCount) {
        this.characterCount = characterCount;
    }

    public int getEstimatedSmsUnits() {
        return estimatedSmsUnits;
    }

    public void setEstimatedSmsUnits(int estimatedSmsUnits) {
        this.estimatedSmsUnits = estimatedSmsUnits;
    }

    public List<String> getIssues() {
        return issues;
    }

    public void setIssues(List<String> issues) {
        this.issues = issues == null ? new ArrayList<>() : issues;
    }

    public String getSuggestedRewrite() {
        return suggestedRewrite;
    }

    public void setSuggestedRewrite(String suggestedRewrite) {
        this.suggestedRewrite = suggestedRewrite;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
