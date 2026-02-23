package com.example.chatbot.config.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.genai.Client;

@Configuration
public class GeminiConfig {

    @Value("${genai.api-key}")
    private String apiKey;

    @Bean
    public Client geminiClient(){
        return Client.builder().apiKey(apiKey).build();
    }

}
