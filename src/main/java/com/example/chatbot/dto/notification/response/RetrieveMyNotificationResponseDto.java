package com.example.chatbot.dto.notification.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveMyNotificationResponseDto {
    private String subject;
    private String notificationId;
    private String message;
    private Boolean read;
}
