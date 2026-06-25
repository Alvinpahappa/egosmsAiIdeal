package com.example.demo.controller;

import com.example.demo.dto.SendTimeRecommendation;
import com.example.demo.service.SendTimeRecommendationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SendTimeControllerTest {

    @Test
    void shouldReturnRecommendation() {
        SendTimeRecommendationService service = mock(SendTimeRecommendationService.class);
        SendTimeRecommendation recommendation = new SendTimeRecommendation();
        recommendation.setCampaignType("marketing");
        recommendation.setRecommendedDay("Friday");
        recommendation.setRecommendedWindow("19:00 - 20:00 EAT");

        when(service.recommend(anyString())).thenReturn(recommendation);

        SendTimeController controller = new SendTimeController(service);
        SendTimeRecommendation result = controller.recommend("marketing");

        assertEquals("Friday", result.getRecommendedDay());
        assertFalse(result.getRecommendedWindow().isBlank());
    }
}
