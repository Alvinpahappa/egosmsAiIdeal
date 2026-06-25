package com.example.demo.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatControllerTest {

    @Test
    void shouldReturnAiResponse() {
        GeminiChatService geminiChatService = mock(GeminiChatService.class);
        when(geminiChatService.askQuestion("Explain HashMap"))
                .thenReturn("HashMap is not thread-safe.");

        ChatController controller = new ChatController(geminiChatService);

        String response = controller.ask("Explain HashMap");

        assertEquals("HashMap is not thread-safe.", response);
    }
}
