package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

// GeminiChatService.java
@Service
public class GeminiChatService {

    private final ChatClient chatClient;

    public GeminiChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String askQuestion(String question) {
        return chatClient
                .prompt(question)
                .call()
                .content();
    }

    public String askWithPersona(String systemPrompt, String userQuestion) {
        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userQuestion)
                .call()
                .content();
    }

    public Flux<String> streamAnswer(String question) {
        return chatClient
                .prompt(question)
                .stream()
                .content();
    }
}