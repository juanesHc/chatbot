package com.example.chatbot.controller.notification;

import com.example.chatbot.dto.notification.request.RetrieveNotificationRequestDto;
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
    public ResponseEntity<List<RetrieveMyNotificationResponseDto>> getNotifications(
            @PathVariable String personId,
            @RequestParam(required = false) String senderId,
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) String role) {

        RetrieveNotificationRequestDto requestDto = new RetrieveNotificationRequestDto(senderId, read, role);
        return ResponseEntity.ok(messagingService.retrieveMyMessages(personId, requestDto));
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<EliminationNotificationResponseDto> deleteNotification(@PathVariable String notificationId){
        return ResponseEntity.ok(messagingService.dropNotification(notificationId));
    }

    @PatchMapping("/read/{notificationId}")
    public ResponseEntity<Void> markAsRead(@PathVariable String notificationId) {
        messagingService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

}
