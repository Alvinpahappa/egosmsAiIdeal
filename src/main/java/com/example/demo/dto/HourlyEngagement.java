package com.example.demo.dto;

public record HourlyEngagement(
        int hour,
        String label,
        long delivered,
        long clicked,
        double clickRate
) {
}
