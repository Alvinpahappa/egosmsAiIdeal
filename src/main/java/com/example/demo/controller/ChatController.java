package com.example.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final GeminiChatService geminiChatService;

    public ChatController(GeminiChatService geminiChatService) {
        this.geminiChatService = geminiChatService;
    }

    @PostMapping("/ask")
    public String ask(@RequestBody String userMessage) {
        return geminiChatService.askQuestion(userMessage);
    }

    @PostMapping("/java-interview")
    public String javaInterview(@RequestBody String question) {
        String systemPrompt = "You are an expert Java interviewer. Answer clearly with code examples. Focus on Java 17+ features and Spring Boot.";
        return geminiChatService.askWithPersona(systemPrompt, question);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam String message) {
        return geminiChatService.streamAnswer(message);
    }
}