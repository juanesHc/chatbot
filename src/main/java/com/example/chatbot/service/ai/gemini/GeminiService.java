package com.example.chatbot.service.ai.gemini;

import com.example.chatbot.dto.chat.response.ChatBotResponseDto;
import com.example.chatbot.dto.message.RegisterMessageRequestDto;
import com.example.chatbot.entity.*;
import com.example.chatbot.exception.GeminiException;
import com.example.chatbot.repository.ChatRepository;
import com.example.chatbot.repository.MemoryRepository;
import com.example.chatbot.repository.MessageRepository;
import com.example.chatbot.service.ai.memory.MemoryService;
import com.example.chatbot.service.ai.memory.PersonGlobalMemoryService;
import com.example.chatbot.service.ai.message.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
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
    private final PersonGlobalMemoryService globalMemoryService;
    private final ObjectMapper objectMapper;

    @Value("${genai.model}")
    private String gemini;

    private static final int MESSAGES_BEFORE_EXTRACTION = 5;

    @Transactional
    public ChatBotResponseDto askGemini(RegisterMessageRequestDto registerMessageRequestDto, String chatId) {

        messageService.registerPersonMessage(registerMessageRequestDto, chatId);

        ChatEntity chatEntity = chatRepository.findById(UUID.fromString(chatId))
                .orElseThrow(() -> new GeminiException("Can't find the chat"));

        PersonEntity person = chatEntity.getPersonEntity();
        List<MemoryEntity> chatMemories = memoryService.getTopMemories(chatEntity, 3);
        List<PersonGlobalMemoryEntity> globalMemories = globalMemoryService.getTopGlobalMemories(person, 5);

        StringBuilder contextMemory = new StringBuilder();

        if (!globalMemories.isEmpty()) {
            contextMemory.append("Important user information (always remember):\n");
            for (PersonGlobalMemoryEntity memory : globalMemories) {
                contextMemory.append("- ")
                        .append(memory.getKey())
                        .append(": ")
                        .append(memory.getValue())
                        .append("\n");
            }
        }

        if (!chatMemories.isEmpty()) {
            contextMemory.append("\nRecent conversation context:\n");
            for (MemoryEntity memory : chatMemories) {
                contextMemory.append("- ")
                        .append(memory.getKey())
                        .append(": ")
                        .append(memory.getValue())
                        .append("\n");
            }
        }

        if (!contextMemory.isEmpty()) {
            contextMemory.append("\nUser message: ");
        }

        String response = generateGeminiResponse(
                contextMemory + registerMessageRequestDto.getMessageContent(),
                chatId
        );

        ChatBotResponseDto chatBotResponseDto = new ChatBotResponseDto();
        chatBotResponseDto.setResponse(response);

        messageService.registerBotMessage(new RegisterMessageRequestDto(response), chatId);

        log.info("Extracting memories for chat {}", chatId);
        extractAndStoreMemoriesFromChat(chatEntity, person);

        return chatBotResponseDto;
    }

    @Transactional
    public void extractAndStoreMemoriesFromChat(ChatEntity chat, PersonEntity person) {

        List<MessageEntity> recentMessages = messageRepository
                .findTopByChatEntityOrderByCreatedAtDesc(chat, PageRequest.of(0, 10));

        String conversationContext = buildConversationContext(recentMessages);

        String prompt = """
        Extract important facts from this conversation as key-value pairs.
        Focus on: user preferences, personal info, important decisions, recurring topics.
        
        CRITICAL: Return ONLY a JSON array, no markdown formatting, no backticks, no extra text.
        
        Format example:
        [
          {"key": "user_name", "value": "John", "importance": 9},
          {"key": "user_preference_food", "value": "loves pizza", "importance": 7}
        ]
        
        Rules:
        - importance must be a number from 1 to 10
        - Return ONLY the JSON array
        - Do NOT wrap in ```json or ``` tags
        - Do NOT add any explanation before or after the JSON
        
        Conversation:
        %s
        """.formatted(conversationContext);

        List<Map<String, Object>> extractedFacts = extractFacts(prompt, String.valueOf(chat.getId()));

        log.info("Processing {} extracted facts for chat {}", extractedFacts.size(), chat.getId());

        for (Map<String, Object> fact : extractedFacts) {
            String key = (String) fact.get("key");
            String value = (String) fact.get("value");
            Integer importance = parseImportance(fact.get("importance"));

            if (key == null || value == null || importance == null) {
                log.warn("Skipping invalid fact: {}", fact);
                continue;
            }

            log.info("Processing fact - key: {}, value: {}, importance: {}", key, value, importance);

            MemoryEntity memory = memoryRepository
                    .findByChatEntityAndKey(chat, key)
                    .orElse(new MemoryEntity());

            boolean isNew = memory.getId() == null;

            memory.setKey(key);
            memory.setValue(value);
            memory.setChatEntity(chat);
            memory.setPriority(memoryService.calculatePriority(importance, isNew));

            memoryRepository.save(memory);
            log.info("Saved chat memory: {} = {} (priority: {})", key, value, memory.getPriority());
        }

        // Guardar memorias globales
        log.info("Storing global memories for person {}", person.getId());
        globalMemoryService.storeGlobalMemories(person, extractedFacts);

        // Eliminar memorias viejas
        memoryService.decayOldMemories(chat);
    }

    private String generateGeminiResponse(String prompt, String chatId) {
        try {
            GenerateContentResponse response =
                    client.models.generateContent(gemini, prompt, null);
            return response.text();
        } catch (Exception e) {
            throw new GeminiException("Error procesando la solicitud: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> extractFacts(String memoryPrompt, String chatId) {
        try {
            String jsonResponse = generateGeminiResponse(memoryPrompt, chatId);

            // LIMPIAR LA RESPUESTA
            String cleanedJson = cleanJsonResponse(jsonResponse);

            log.info("Raw response from Gemini: {}", jsonResponse);
            log.info("Cleaned JSON to parse: {}", cleanedJson);

            List<Map<String, Object>> facts = objectMapper.readValue(
                    cleanedJson,
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            log.info("Extracted {} facts from conversation in chat {}", facts.size(), chatId);
            return facts;

        } catch (JsonProcessingException e) {
            log.error("Error parsing Gemini response as JSON: {}", e.getMessage());
            log.error("Failed response: {}", e.getOriginalMessage());
            return List.of();
        } catch (Exception e) {
            log.error("Error extracting facts: {}", e.getMessage(), e);
            return List.of();
        }
    }

    // MÉTODO PARA LIMPIAR LA RESPUESTA DE GEMINI
    private String cleanJsonResponse(String response) {
        if (response == null || response.isEmpty()) {
            return "[]";
        }

        String cleaned = response.trim();

        // Remover ```json al inicio
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }

        // Remover ``` al final
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }

        cleaned = cleaned.trim();

        // Validar que sea JSON
        if (cleaned.isEmpty() || (!cleaned.startsWith("[") && !cleaned.startsWith("{"))) {
            log.warn("Response después de limpiar no es JSON válido: {}", cleaned);
            return "[]";
        }

        return cleaned;
    }

    // MÉTODO PARA PARSEAR IMPORTANCE DE FORMA SEGURA
    private Integer parseImportance(Object importanceObj) {
        if (importanceObj == null) {
            log.debug("Importance is null, using default value 5");
            return 5;
        }

        if (importanceObj instanceof Integer) {
            return (Integer) importanceObj;
        }

        if (importanceObj instanceof Double) {
            return ((Double) importanceObj).intValue();
        }

        if (importanceObj instanceof String) {
            try {
                return Integer.parseInt((String) importanceObj);
            } catch (NumberFormatException e) {
                log.warn("Invalid importance string value: {}, using default 5", importanceObj);
                return 5;
            }
        }

        if (importanceObj instanceof Number) {
            return ((Number) importanceObj).intValue();
        }

        log.warn("Unknown importance type: {}, using default 5", importanceObj.getClass());
        return 5;
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