package com.example.chatbot.service.chat;

import com.example.chatbot.dto.chat.request.RegisterChatNameRequestDto;
import com.example.chatbot.dto.chat.response.RegisterChatNameResponseDto;
import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.mapper.chat.ChatMapper;
import com.example.chatbot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterChatNameService {

    private final ChatMapper chatMapper;
    private final ChatRepository chatRepository;

    public RegisterChatNameResponseDto registerChat(RegisterChatNameRequestDto registerChatNameRequestDto,String personId){
        ChatEntity chatEntity=chatMapper.registerChatNameRequestDtoToChatEntity(registerChatNameRequestDto,personId);
        chatRepository.save(chatEntity);

        log.info("chat name saved");
        return new RegisterChatNameResponseDto("Chat successful register with name:"+chatEntity.getName());
    }


}
