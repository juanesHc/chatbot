package com.example.chatbot.dto.notification.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RetrieveSentMessagesResponseDto {

    private String subject;
    private String notificationId;
    private String message;
    private Boolean read;
    private String receiverName;

}
