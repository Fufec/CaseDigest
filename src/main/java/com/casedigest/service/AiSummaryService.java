package com.casedigest.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiSummaryService {
    private final ChatClient chatClient;

    // Load the prompt template from application.properties
    @Value("${app.ai.prompt-template}")
    private String promptTemplate;

    public AiSummaryService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("Jsi zkušený právní asistent. Tvým úkolem je analyzovat české právní dokumenty a vytvářet jejich stručná a srozumitelná shrnutí. Ignoruj formátovací znaky.")
                .defaultOptions(GoogleGenAiChatOptions.builder()
                        .temperature(0.1) // low temperature to make the AI more deterministic
                        .build())
                .build();
    }

    public String summarize(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Dokument neobsahuje žádný text.");
        }

        String promptText = promptTemplate.formatted(text);

        return chatClient.prompt()
                .user(promptText)
                .call()
                .content();
    }
}
