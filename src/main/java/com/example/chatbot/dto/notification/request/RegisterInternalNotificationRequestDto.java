package com.example.chatbot.dto.notification.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RegisterInternalNotificationRequestDto {
    private String subject;
    private String messageDescription;
}
