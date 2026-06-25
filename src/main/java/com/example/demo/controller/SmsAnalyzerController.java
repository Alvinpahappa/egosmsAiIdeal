package com.example.demo.controller;

import com.example.demo.dto.SmsAnalyzeRequest;
import com.example.demo.dto.SmsAnalyzeResponse;
import com.example.demo.service.SmsAnalyzerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/sms")
public class SmsAnalyzerController {

    private final SmsAnalyzerService smsAnalyzerService;

    public SmsAnalyzerController(SmsAnalyzerService smsAnalyzerService) {
        this.smsAnalyzerService = smsAnalyzerService;
    }

    @PostMapping("/analyze")
    public SmsAnalyzeResponse analyze(@RequestBody SmsAnalyzeRequest request) {
        return smsAnalyzerService.analyze(request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }
}
