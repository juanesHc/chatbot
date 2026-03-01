package com.example.chatbot.mapper.message;

import com.example.chatbot.dto.message.request.RegisterMessageRequestDto;
import com.example.chatbot.dto.message.response.RetrieveMessageResponseDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MessageEntity;
import com.example.chatbot.entity.enums.MessageBotRoleEnum;
import com.example.chatbot.exception.MessageException;
import com.example.chatbot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final ChatRepository chatRepository;

    public MessageEntity registerMessageRequestDtoToMessageEntity(RegisterMessageRequestDto registerMessageRequestDto, MessageBotRoleEnum messageBotRoleEnum, String chatId){

        MessageEntity messageEntity=new MessageEntity();
        ChatEntity chatEntity=chatRepository.findById(UUID.fromString(chatId)).
                orElseThrow(()-> new MessageException("Chat not found"));

        messageEntity.setMessageContent(registerMessageRequestDto.getMessageContent());
        messageEntity.setMessageBotRoleEnum(messageBotRoleEnum);
        messageEntity.setChatEntity(chatEntity);

        return messageEntity;
    }

    public RetrieveMessageResponseDto messageEntityToRetrieveMessageDto(MessageEntity messageEntity){

        RetrieveMessageResponseDto retrieveMessageResponseDto=new RetrieveMessageResponseDto();

        retrieveMessageResponseDto.setType(messageEntity.getMessageBotRoleEnum());
        retrieveMessageResponseDto.setContent(messageEntity.getMessageContent());
        retrieveMessageResponseDto.setTimestamp(messageEntity.getCreatedAt());

        return retrieveMessageResponseDto;

    }


}
