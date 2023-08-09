package com.oam.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChatGptService {

    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public String getSummary(String text) {
        ChatRequest request = new ChatRequest(model, getPrompt(text));
        ChatResponse response = getRestTemplate().postForObject(apiUrl, request, ChatResponse.class);
        if (response == null || response.choices.isEmpty()) {
            return "Could not generate the summary";
        }
        return response.choices().get(0).message().content();
    }

    private String getPrompt(String text) {
        return "Make a short summary: " + text;
    }

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    private record Message(
            String role,
            String content
    ) { }

    private record ChatRequest(
            String model,
            List<Message> messages,

            @JsonProperty("max_tokens")
            Integer maxTokens
    ) {
        public ChatRequest(String model, String text) {
            this(model, List.of(new Message("user", text)), 40);
        }
    }

    private record Choice(
            int index,
            Message message
    ) { }

    private record ChatResponse(
            List<Choice> choices
    ) { }
}
