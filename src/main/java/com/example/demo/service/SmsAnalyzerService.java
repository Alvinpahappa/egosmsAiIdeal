package com.example.demo.service;

import com.example.demo.controller.GeminiChatService;
import com.example.demo.dto.SmsAnalyzeRequest;
import com.example.demo.dto.SmsAnalyzeResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SmsAnalyzerService {

    private static final Pattern JSON_BLOCK = Pattern.compile("```(?:json)?\\s*([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    private final GeminiChatService geminiChatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SmsAnalyzerService(GeminiChatService geminiChatService) {
        this.geminiChatService = geminiChatService;
    }

    public SmsAnalyzeResponse analyze(SmsAnalyzeRequest request) {
        if (request.message() == null || request.message().isBlank()) {
            throw new IllegalArgumentException("Message is required");
        }

        String systemPrompt = """
                You are an SMS message analyst for EgoSMS, Uganda's bulk SMS platform.
                Respond with valid JSON only. No markdown fences or extra text.
                Use this exact schema:
                {
                  "detectedTone": "string",
                  "clarityScore": 1-10,
                  "segmentCount": number,
                  "characterCount": number,
                  "estimatedSmsUnits": number,
                  "issues": ["string"],
                  "suggestedRewrite": "string",
                  "summary": "one sentence overview"
                }
                Rules:
                - GSM SMS segments are 160 characters each (count for standard Latin SMS).
                - Flag spam triggers (FREE, WIN, URGENT, excessive punctuation).
                - Marketing SMS should mention opt-out when appropriate.
                - Prefer clear dates and one call-to-action.
                - suggestedRewrite must match the user's preferred tone.
                - Keep rewrites within maxSegments when possible.
                """;

        String userPrompt = """
                Analyze this draft SMS:
                ---
                %s
                ---
                Preferred tone: %s
                Max segments: %d
                """.formatted(
                request.message().trim(),
                request.toneOrDefault(),
                request.maxSegmentsOrDefault()
        );

        String raw = geminiChatService.askWithPersona(systemPrompt, userPrompt);
        return parseResponse(raw, request.message());
    }

    private SmsAnalyzeResponse parseResponse(String raw, String originalMessage) {
        SmsAnalyzeResponse response = new SmsAnalyzeResponse();
        response.setCharacterCount(originalMessage.length());
        response.setSegmentCount(estimateSegments(originalMessage.length()));
        response.setEstimatedSmsUnits(response.getSegmentCount());

        try {
            JsonNode node = objectMapper.readTree(extractJson(raw));
            response.setDetectedTone(textOrDefault(node, "detectedTone", "unknown"));
            response.setClarityScore(node.path("clarityScore").asInt(5));
            response.setSegmentCount(node.path("segmentCount").asInt(response.getSegmentCount()));
            response.setCharacterCount(node.path("characterCount").asInt(response.getCharacterCount()));
            response.setEstimatedSmsUnits(node.path("estimatedSmsUnits").asInt(response.getSegmentCount()));
            response.setSuggestedRewrite(textOrDefault(node, "suggestedRewrite", originalMessage));
            response.setSummary(textOrDefault(node, "summary", "Analysis complete."));
            response.setIssues(readIssues(node));
            return response;
        } catch (Exception ex) {
            response.setDetectedTone("unknown");
            response.setClarityScore(5);
            response.setSummary("AI analysis returned unstructured output. See suggested rewrite below.");
            response.setSuggestedRewrite(raw.trim());
            response.getIssues().add("Could not parse structured analysis. Raw AI output used as suggestion.");
            return response;
        }
    }

    private List<String> readIssues(JsonNode node) {
        List<String> issues = new ArrayList<>();
        JsonNode issuesNode = node.path("issues");
        if (issuesNode.isArray()) {
            issuesNode.forEach(item -> {
                if (!item.asText().isBlank()) {
                    issues.add(item.asText());
                }
            });
        }
        return issues;
    }

    private String textOrDefault(JsonNode node, String field, String fallback) {
        String value = node.path(field).asText("");
        return value.isBlank() ? fallback : value;
    }

    private String extractJson(String raw) {
        Matcher matcher = JSON_BLOCK.matcher(raw);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return raw.trim();
    }

    private int estimateSegments(int length) {
        if (length <= 0) {
            return 0;
        }
        return (int) Math.ceil(length / 160.0);
    }
}
