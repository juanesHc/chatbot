package com.example.chatbot.dto.message;


import com.example.chatbot.entity.enums.MessageRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RetrieveMessageResponseDto {
    private String content;
    private MessageRoleEnum type;
    private LocalDateTime timestamp;
}
