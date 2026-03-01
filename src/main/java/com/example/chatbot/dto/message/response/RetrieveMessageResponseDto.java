package com.example.chatbot.dto.message.response;


import com.example.chatbot.entity.enums.MessageBotRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RetrieveMessageResponseDto {
    private String content;
    private MessageBotRoleEnum type;
    private LocalDateTime timestamp;
}
