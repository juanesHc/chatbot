package com.example.chatbot.service.ai.gemini;

import com.example.chatbot.dto.chat.request.ChatBotRequestDto;
import com.example.chatbot.dto.chat.response.ChatBotResponseDto;
import com.example.chatbot.dto.message.request.RegisterMessageRequestDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MemoryEntity;
import com.example.chatbot.entity.MessageEntity;
import com.example.chatbot.exception.GeminiException;
import com.example.chatbot.repository.ChatRepository;
import com.example.chatbot.repository.MessageRepository;
import com.example.chatbot.service.ai.memory.MemoryService;
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
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    private final MemoryService memoryService;
    private final MessageService messageService;
    private final Client client;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Value("${genai.model}")
    private String gemini;

    public ChatBotResponseDto askGemini(RegisterMessageRequestDto registerMessageRequestDto,String chatId) {
        messageService.registerPersonMessage(registerMessageRequestDto,chatId);

        ChatEntity chatEntity=chatRepository.findById(UUID.fromString(chatId)).
                orElseThrow(()->new GeminiException("Cant found the chat"));

        List<MemoryEntity> memories=memoryService.getTopMemories(chatEntity,5);

        StringBuilder contextMemory=new StringBuilder();
        if(!memories.isEmpty()){
            contextMemory.append("CONTEXT");
            for(MemoryEntity memory:memories){
                contextMemory.append(memory.getKey()).append(memory.getValue());

            }
        }

        String response=generateGeminiResponse(contextMemory+registerMessageRequestDto.getMessageContent(),chatId);
        ChatBotResponseDto chatBotResponseDto=new ChatBotResponseDto();
        chatBotResponseDto.setResponse(response);

        messageService.registerBotMessage(new RegisterMessageRequestDto(response),chatId);

        List<MessageEntity> messageEntities=messageRepository.findAll();
        memoryService.extractAndStoreMemories(chatEntity,messageEntities);

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
