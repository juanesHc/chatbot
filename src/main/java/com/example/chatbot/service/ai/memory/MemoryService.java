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

    public List<MemoryEntity> getTopMemories(ChatEntity chat, int limit) {
        return memoryRepository.findTopByChatEntityOrderByPriorityDesc(chat, limit);
    }

    @Transactional
    public void decayOldMemories(ChatEntity chat) {
        List<MemoryEntity> allMemories = memoryRepository.findByChatEntity(chat);

        for (MemoryEntity memory : allMemories) {
            if(memory.getPriority()<60){
                memoryRepository.delete(memory);
            }
        }

        memoryRepository.saveAll(allMemories);
    }

    public Integer calculatePriority(Integer importance, boolean isNew) {
        int priority = importance * 10;
        if (isNew) {
            priority += 20;
        }
        return priority;
    }



}
