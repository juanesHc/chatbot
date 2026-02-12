package com.example.chatbot.mapper.message;

import com.example.chatbot.dto.message.RegisterMessageRequestDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MessageEntity;
import com.example.chatbot.entity.enums.MessageRoleEnum;
import com.example.chatbot.exception.MessageException;
import com.example.chatbot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final ChatRepository chatRepository;

    public MessageEntity registerMessageRequestDtoToMessageEntity(RegisterMessageRequestDto registerMessageRequestDto, MessageRoleEnum messageRoleEnum,String chatId){

        MessageEntity messageEntity=new MessageEntity();
        ChatEntity chatEntity=chatRepository.findById(UUID.fromString(chatId)).
                orElseThrow(()-> new MessageException("Chat not found"));

        messageEntity.setMessageContent(registerMessageRequestDto.getMessageContent());
        messageEntity.setMessageRoleEnum(messageRoleEnum);
        messageEntity.setChatEntity(chatEntity);

        return messageEntity;


    }


}
