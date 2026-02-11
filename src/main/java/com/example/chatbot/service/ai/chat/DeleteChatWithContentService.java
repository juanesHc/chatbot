package com.example.chatbot.service.ai.chat;

import com.example.chatbot.dto.chat.response.DeleteChatResponseDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MemoryEntity;
import com.example.chatbot.entity.MessageEntity;
import com.example.chatbot.exception.ChatException;
import com.example.chatbot.repository.ChatRepository;
import com.example.chatbot.repository.MemoryRepository;
import com.example.chatbot.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteChatWithContentService {

    private final MemoryRepository memoryRepository;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public DeleteChatResponseDto deleteChatService(String chatId){

        ChatEntity chatEntity=chatRepository.findById(UUID.fromString(chatId)).
                orElseThrow(()-> new ChatException("Run into a problem while trying to find chat"));
        List<MemoryEntity> memoriesToDrop=memoryRepository.findByChatEntity(chatEntity);
        List<MessageEntity> messagesToDrop=messageRepository.findByChatEntity(chatEntity);

        memoryRepository.deleteAllByChatEntity(chatEntity);
        messageRepository.deleteAllByChatEntity(chatEntity);
        chatRepository.delete(chatEntity);

        return new DeleteChatResponseDto("Chat successfully drop");
    }



}
