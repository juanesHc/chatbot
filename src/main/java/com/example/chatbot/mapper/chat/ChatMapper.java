package com.example.chatbot.mapper.chat;

import com.example.chatbot.dto.chat.request.RegisterChatNameRequestDto;
import com.example.chatbot.entity.ChatEntity;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public ChatEntity registerChatRequestDtoToChatEntity(RegisterChatNameRequestDto chatRequestDto){
        ChatEntity chatEntity=new ChatEntity();
        chatEntity.setName(chatRequestDto.getName());
        return chatEntity;
    }


}
