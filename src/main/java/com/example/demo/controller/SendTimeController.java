package com.example.demo.controller;

import com.example.demo.dto.SendTimeRecommendation;
import com.example.demo.service.SendTimeRecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/send-time")
public class SendTimeController {

    private final SendTimeRecommendationService sendTimeRecommendationService;

    public SendTimeController(SendTimeRecommendationService sendTimeRecommendationService) {
        this.sendTimeRecommendationService = sendTimeRecommendationService;
    }

    @GetMapping("/recommend")
    public SendTimeRecommendation recommend(
            @RequestParam(defaultValue = "marketing") String campaignType
    ) {
        return sendTimeRecommendationService.recommend(campaignType);
    }
}
