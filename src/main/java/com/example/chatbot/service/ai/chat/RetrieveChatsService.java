package com.example.chatbot.service.ai.chat;

import com.example.chatbot.dto.chat.response.RetrieveChatsNameResponseDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.mapper.chat.ChatMapper;
import com.example.chatbot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetrieveChatsService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    public List<RetrieveChatsNameResponseDto> retrieveChatsName(String personId){
        List<ChatEntity> chatsEntity=chatRepository.findByPersonEntityId(UUID.fromString(personId));

        return chatMapper.chatEntityToRetrieveDto(chatsEntity);
    }

}
