package com.example.chatbot.service.ai.gemini;

import com.example.chatbot.dto.chat.request.ChatBotRequestDto;
import com.example.chatbot.dto.chat.response.ChatBotResponseDto;
import com.example.chatbot.dto.message.request.RegisterMessageRequestDto;
import com.example.chatbot.exception.GeminiException;
import com.example.chatbot.service.ai.message.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    private final MessageService messageService;
    private final Client client;

    @Value("${genai.model}")
    private String gemini;

    public ChatBotResponseDto askGemini(RegisterMessageRequestDto registerMessageRequestDto,String chatId) {
        messageService.registerPersonMessage(registerMessageRequestDto,chatId);

        String response=generateGeminiResponse(registerMessageRequestDto.getMessageContent(),chatId);
        ChatBotResponseDto chatBotResponseDto=new ChatBotResponseDto();
        chatBotResponseDto.setResponse(response);


        messageService.registerBotMessage(new RegisterMessageRequestDto(response),chatId);

        return chatBotResponseDto;

    }

    private String generateGeminiResponse(String prompt,String chatId) {
        try {
            GenerateContentResponse response =
                    client.models.generateContent(gemini, prompt, null);


            return (response.text());

        } catch (Exception e) {
            throw  new GeminiException("Error procesando la solicitud: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> extractFacts(String memoryPrompt, String chatId){
        try {
            String jsonResponse = generateGeminiResponse(memoryPrompt, chatId);

            String cleanJson = jsonResponse
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> facts = objectMapper.readValue(
                    cleanJson,
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            log.info("Extracted {} facts from conversation in chat {}", facts.size(), chatId);
            return facts;

        } catch (JsonProcessingException e) {
            log.error("Error parsing Gemini response as JSON: {}", e.getMessage());
            return List.of();
        } catch (Exception e) {
            log.error("Error extracting facts: {}", e.getMessage());
            return List.of();
        }
    }


}
