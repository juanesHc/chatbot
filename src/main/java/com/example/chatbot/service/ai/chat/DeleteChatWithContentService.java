package com.example.chatbot.service.ai.chat;

import com.example.chatbot.repository.ChatRepository;
import com.example.chatbot.repository.MemoryRepository;
import com.example.chatbot.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteChatWithContentService {

    private final MemoryRepository memoryRepository;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public void deleteChat(String chatId){

    }



}
