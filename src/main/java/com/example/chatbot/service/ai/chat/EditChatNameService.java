package com.example.chatbot.service.ai.chat;

import com.example.chatbot.dto.chat.request.RegisterChatNameRequestDto;
import com.example.chatbot.dto.chat.response.RegisterChatNameResponseDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.exception.RegisterChatNameException;
import com.example.chatbot.mapper.chat.ChatMapper;
import com.example.chatbot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EditChatNameService {

    private final ChatMapper chatMapper;
    private final ChatRepository chatRepository;

    public RegisterChatNameResponseDto registerChat(RegisterChatNameRequestDto registerChatNameRequestDto, String chatId){
        ChatEntity chatEntity=chatMapper.editChatNameRequestDtoToChatEntity(registerChatNameRequestDto,chatId);
        chatRepository.save(chatEntity);
        if(validateNameIsUnique(registerChatNameRequestDto.getName())){
            throw new RegisterChatNameException("Chats name must be unique");
        }

        log.info("chat name updated");
        return new RegisterChatNameResponseDto("Chat successful register with name:"+chatEntity.getName());
    }

    private Boolean validateNameIsUnique(String name){
        return chatRepository.existsByName(name);
    }

}
