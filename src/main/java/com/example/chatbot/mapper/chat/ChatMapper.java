package com.example.chatbot.mapper.chat;

import com.example.chatbot.dto.chat.request.RegisterChatNameRequestDto;
import com.example.chatbot.dto.chat.response.RetrieveChatsNameResponseDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.exception.RegisterChatNameException;
import com.example.chatbot.repository.ChatRepository;
import com.example.chatbot.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    private final PersonRepository personRepository;
    private final ChatRepository chatRepository;

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

        responseDto.setName(name);
        return responseDto;
    }
    public ChatEntity editChatNameRequestDtoToChatEntity(RegisterChatNameRequestDto chatRequestDto,String chatId){

        ChatEntity chatEntity=chatRepository.findById(UUID.fromString(chatId)).
                orElseThrow(()->new RegisterChatNameException("Not Found person"));

        chatEntity.setName(chatRequestDto.getName());
        return chatEntity;
    }

    public List<RetrieveChatsNameResponseDto> chatEntityToRetrieveDto(List<ChatEntity> chatEntity){
        List<RetrieveChatsNameResponseDto> responseDtoList=new ArrayList<>();

        for (ChatEntity chat:chatEntity){
            String id= String.valueOf(chat.getId());
            String name=chat.getName();

            RetrieveChatsNameResponseDto responseDto=new RetrieveChatsNameResponseDto();
            responseDto.setId(id);
            responseDto.setName(name);

            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }


}
