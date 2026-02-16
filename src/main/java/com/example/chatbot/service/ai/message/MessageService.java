package com.example.chatbot.service.ai.message;

import com.example.chatbot.dto.message.RegisterMessageRequestDto;
import com.example.chatbot.dto.message.RetrieveMessageResponseDto;
import com.example.chatbot.entity.MessageEntity;
import com.example.chatbot.entity.enums.MessageRoleEnum;
import com.example.chatbot.mapper.message.MessageMapper;
import com.example.chatbot.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;

    public void registerPersonMessage(RegisterMessageRequestDto registerMessageRequestDto,String chatId){
        registerMessage(registerMessageRequestDto,MessageRoleEnum.PERSON,chatId);
    }

    public void registerBotMessage(RegisterMessageRequestDto registerMessageRequestDto,String chatId){
        registerMessage(registerMessageRequestDto,MessageRoleEnum.BOT,chatId);
    }

    private void registerMessage(RegisterMessageRequestDto registerMessageRequestDto, MessageRoleEnum roleEnum,String chatId){

        MessageEntity messageEntity=messageMapper.registerMessageRequestDtoToMessageEntity(registerMessageRequestDto,roleEnum,chatId);
        messageRepository.save(messageEntity);
    }

    @Transactional
    public List<RetrieveMessageResponseDto> getMessagesByChatId(String chatId) {
        List<MessageEntity> messages = messageRepository.findByChatIdOrderByTimestampAsc(UUID.fromString(chatId));
        List<RetrieveMessageResponseDto> responseDtos= new ArrayList<>();

        for(MessageEntity messageEntity:messages){
           RetrieveMessageResponseDto responseDto= messageMapper.messageEntityToRetrieveMessageDto(messageEntity);
           responseDtos.add(responseDto);
        }

        return responseDtos;

    }

}
