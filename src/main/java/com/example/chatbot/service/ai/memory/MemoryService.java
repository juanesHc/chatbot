package com.example.chatbot.service.ai.memory;

import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MemoryEntity;
import com.example.chatbot.entity.MessageEntity;
import com.example.chatbot.repository.MemoryRepository;
import com.example.chatbot.service.ai.gemini.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MemoryService {
    private final MemoryRepository memoryRepository;
    private final GeminiService geminiService; // Your Gemini API wrapper

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

        List<Map<String, Object>> extractedFacts = geminiService.extractFacts(prompt, String.valueOf(chat.getId()));

        for (Map<String, Object> fact : extractedFacts) {
            String key = (String) fact.get("key");
            String value = (String) fact.get("value");
            Integer importance = (Integer) fact.get("importance");

            MemoryEntity memory = memoryRepository
                    .findByChatEntityAndKey(chat, key);


            memory.setKey(key);
            memory.setValue(value);
            memory.setChatEntity(chat);

            memory.setPriority(calculatePriority(importance, true));

            memoryRepository.save(memory);
        }

        decayOldMemories(chat);
    }

    public List<MemoryEntity> getTopMemories(ChatEntity chat, int limit) {
        return memoryRepository.findTopByChatEntityOrderByPriorityDesc(chat, limit);
    }

    private Integer calculatePriority(Integer importance, boolean isNew) {
        int priority = importance * 10; // Scale importance
        if (isNew) {
            priority += 20; // Recency boost for new memories
        }
        return priority;
    }

    @Transactional
    public void decayOldMemories(ChatEntity chat) {
        List<MemoryEntity> allMemories = memoryRepository.findByChatEntity(chat);

        for (MemoryEntity memory : allMemories) {
            // Decay by 5% (you can adjust this)
            int newPriority = (int) (memory.getPriority() * 0.95);
            memory.setPriority(Math.max(newPriority, 1)); // Minimum priority of 1
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

    /**
     * Format memories for Gemini context
     */
    public String formatMemoriesForContext(List<MemoryEntity> memories) {
        if (memories.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("Previous context:\n");
        for (MemoryEntity memory : memories) {
            sb.append("- ").append(memory.getKey()).append(": ")
                    .append(memory.getValue()).append("\n");
        }
        return sb.toString();
    }
}
