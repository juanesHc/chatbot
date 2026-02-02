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



}
