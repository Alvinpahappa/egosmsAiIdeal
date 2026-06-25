package com.example.demo.controller;

import com.example.demo.dto.SmsAnalyzeRequest;
import com.example.demo.dto.SmsAnalyzeResponse;
import com.example.demo.service.SmsAnalyzerService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SmsAnalyzerControllerTest {

    @Test
    void shouldReturnAnalysis() {
        SmsAnalyzerService service = mock(SmsAnalyzerService.class);
        SmsAnalyzeResponse response = new SmsAnalyzeResponse();
        response.setDetectedTone("promotional");
        response.setClarityScore(4);
        response.setSuggestedRewrite("Your offer expires 26 Jun. Visit egosms.co. Reply STOP to opt out.");

        when(service.analyze(any(SmsAnalyzeRequest.class))).thenReturn(response);

        SmsAnalyzerController controller = new SmsAnalyzerController(service);
        SmsAnalyzeRequest request = new SmsAnalyzeRequest("FREE bonus now!!!", "professional", 1);

        SmsAnalyzeResponse result = controller.analyze(request);

        assertEquals("promotional", result.getDetectedTone());
        assertEquals(4, result.getClarityScore());
    }
}
