package com.example.chatbot.service.chat;

import com.example.chatbot.dto.chat.response.RetrieveChatsNameResponseDto;
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
        List<String> chatsName=chatRepository.findChatNameByPersonId(UUID.fromString(personId));

        List<RetrieveChatsNameResponseDto> responseDtoList =new ArrayList<>();

        chatsName.forEach(entity->responseDtoList.add(
                chatMapper.retrieveChatsEntityToResponseDto(entity)
        ));

        return responseDtoList;

    }

}
