package com.example.chatbot.service.bot;

import com.example.chatbot.dto.chat.request.ChatBotRequestDto;
import com.example.chatbot.dto.chat.response.ChatBotResponseDto;
import com.example.chatbot.exception.GeminiException;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {


    private final Client client;

    @Value("${genai.model}")
    private String gemini;

    public ChatBotResponseDto askGemini(ChatBotRequestDto chatBotRequestDto) {
        return generateGeminiResponse(chatBotRequestDto.getPrompt());

    }

    private ChatBotResponseDto generateGeminiResponse(String prompt) {
        try {
            GenerateContentResponse response =
                    client.models.generateContent(gemini, prompt, null);

            return new ChatBotResponseDto(response.text());

        } catch (Exception e) {
            throw  new GeminiException("Error procesando la solicitud: " + e.getMessage());
        }
    }


}
