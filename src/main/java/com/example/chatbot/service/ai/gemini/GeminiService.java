package com.example.chatbot.service.ai.gemini;

import com.example.chatbot.dto.chat.request.ChatBotRequestDto;
import com.example.chatbot.dto.chat.response.ChatBotResponseDto;
import com.example.chatbot.dto.message.request.RegisterMessageRequestDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MemoryEntity;
import com.example.chatbot.entity.MessageEntity;
import com.example.chatbot.exception.GeminiException;
import com.example.chatbot.repository.ChatRepository;
import com.example.chatbot.repository.MemoryRepository;
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
import org.springframework.transaction.annotation.Transactional;

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
    private final MemoryRepository memoryRepository;

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
        extractAndStoreMemories(chatEntity,messageEntities);

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

    @Transactional
    public void extractAndStoreMemories(ChatEntity chat, List<MessageEntity> recentMessages) {

        String conversationContext = buildConversationContext(recentMessages);

        String prompt = """
            Extract important facts from this conversation as key-value pairs.
            Focus on: user preferences, personal info, important decisions, recurring topics.
            
            Format your response as JSON:
            [
              {"key": "user_name", "value": "John", "importance": 9},
              {"key": "user_preference_food", "value": "loves pizza", "importance": 7}
            ]
            
            Conversation:
            %s
            """.formatted(conversationContext);

        List<Map<String, Object>> extractedFacts =extractFacts(prompt, String.valueOf(chat.getId()));

        for (Map<String, Object> fact : extractedFacts) {
            String key = (String) fact.get("key");
            String value = (String) fact.get("value");
            Integer importance = (Integer) fact.get("importance");

            MemoryEntity memory = new MemoryEntity();

            memory.setKey(key);
            memory.setValue(value);
            memory.setChatEntity(chat);

            memory.setPriority(calculatePriority(importance, true));

            memoryRepository.save(memory);}


        decayOldMemories(chat);
    }

    private Integer calculatePriority(Integer importance, boolean isNew) {
        int priority = importance * 10;
        if (isNew) {
            priority += 20;
        }
        return priority;
    }

    @Transactional
    public void decayOldMemories(ChatEntity chat) {
        List<MemoryEntity> allMemories = memoryRepository.findByChatEntity(chat);

        for (MemoryEntity memory : allMemories) {
            // Decay by 5% (you can adjust this)
            int newPriority = (int) (memory.getPriority() * 0.95);
            memory.setPriority(Math.max(newPriority, 1));
        }

        memoryRepository.saveAll(allMemories);
    }

    private String buildConversationContext(List<MessageEntity> messages) {
        StringBuilder sb = new StringBuilder();
        for (MessageEntity msg : messages) {
            sb.append(msg.getMessageRoleEnum()).append(": ")
                    .append(msg.getMessageContent()).append("\n");
        }
        return sb.toString();
    }


}
