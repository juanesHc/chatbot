package com.example.chatbot.controller.notification;

import com.example.chatbot.dto.notification.response.EliminationNotificationResponseDto;
import com.example.chatbot.dto.notification.response.RetrieveMyNotificationResponseDto;
import com.example.chatbot.service.messaging.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final MessagingService messagingService;

    @GetMapping("/retrieve/{personId}")
    public ResponseEntity<List<RetrieveMyNotificationResponseDto>> getNotifications(@PathVariable String personId){
        return ResponseEntity.ok(messagingService.retrieveMyMessages(personId));
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<EliminationNotificationResponseDto> deleteNotification(@PathVariable String notificationId){
        return ResponseEntity.ok(messagingService.dropNotification(notificationId));
    }

}
