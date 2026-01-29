package com.example.chatbot.mapper.chat;

import com.example.chatbot.dto.chat.request.RegisterChatNameRequestDto;
import com.example.chatbot.dto.chat.response.RetrieveChatsNameResponseDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.exception.RegisterChatNameException;
import com.example.chatbot.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    private final PersonRepository personRepository;

    public ChatEntity registerChatNameRequestDtoToChatEntity(RegisterChatNameRequestDto chatRequestDto,String personId){
        ChatEntity chatEntity=new ChatEntity();
        chatEntity.setName(chatRequestDto.getName());

        PersonEntity personEntity=personRepository.findById(UUID.fromString(personId)).
                orElseThrow(()->new RegisterChatNameException("Not Found person"));

        chatEntity.setPersonEntity(personEntity);
        return chatEntity;
    }

    public RetrieveChatsNameResponseDto retrieveChatsEntityToResponseDto(String name){
        RetrieveChatsNameResponseDto responseDto=new RetrieveChatsNameResponseDto();

        responseDto.setChatName(name);
        return responseDto;
    }


}
