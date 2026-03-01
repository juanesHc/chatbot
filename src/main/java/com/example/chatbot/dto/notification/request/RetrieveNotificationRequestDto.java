package com.example.chatbot.dto.notification.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RetrieveNotificationRequestDto {
    private String senderId;
    private Boolean read;
    private String role;
}
