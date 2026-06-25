package com.example.demo.dto;

public record SmsAnalyzeRequest(
        String message,
        String tone,
        Integer maxSegments
) {
    public String toneOrDefault() {
        return tone == null || tone.isBlank() ? "professional" : tone;
    }

    public int maxSegmentsOrDefault() {
        return maxSegments == null || maxSegments < 1 ? 2 : maxSegments;
    }
}
