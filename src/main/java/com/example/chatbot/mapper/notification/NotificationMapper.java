package com.example.chatbot.mapper.notification;

import com.example.chatbot.dto.notification.request.RegisterInternalNotificationRequestDto;
import com.example.chatbot.dto.notification.request.RegisterNotificationRequestDto;
import com.example.chatbot.dto.notification.response.RetrieveMyNotificationResponseDto;
import com.example.chatbot.dto.notification.response.RetrieveSentMessagesResponseDto;
import com.example.chatbot.entity.NotificationEntity;
import com.example.chatbot.entity.PersonEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    public NotificationEntity registerNotificationRequestDtoToEntity(PersonEntity personEntity,
                                                                     RegisterNotificationRequestDto requestDto){
        NotificationEntity notificationEntity=new NotificationEntity();
        notificationEntity.setReceiver(personEntity);
        notificationEntity.setSubject(requestDto.getSubject());
        notificationEntity.setDescription(requestDto.getMessageDescription());
        notificationEntity.setRead(false);

        return notificationEntity;
    }

    public NotificationEntity registerInternalNotificationRequestDtoToEntity(PersonEntity personEntity,
                                                                     RegisterInternalNotificationRequestDto requestDto){
        NotificationEntity notificationEntity=new NotificationEntity();
        notificationEntity.setReceiver(personEntity);
        notificationEntity.setSubject(requestDto.getSubject());
        notificationEntity.setDescription(requestDto.getMessageDescription());
        notificationEntity.setRead(false);

        return notificationEntity;
    }

    public RetrieveMyNotificationResponseDto notificationEntityToRetrieveNotificationResponseDto(
            NotificationEntity notificationEntity) {

        RetrieveMyNotificationResponseDto dto = new RetrieveMyNotificationResponseDto();
        dto.setMessage(notificationEntity.getDescription());
        dto.setNotificationId(String.valueOf(notificationEntity.getId()));
        dto.setSubject(notificationEntity.getSubject());
        dto.setRead(notificationEntity.isRead());

        if (notificationEntity.getSender() != null) {
            dto.setSenderName(notificationEntity.getSender().getFirstName() + " " + notificationEntity.getSender().getLastName());
            dto.setSenderId(String.valueOf(notificationEntity.getSender().getId()));
        } else {
            dto.setSenderName("Sistema");
            dto.setSenderId(null);
        }

        return dto;
    }

    public RetrieveSentMessagesResponseDto mapToSentMessageDto(NotificationEntity entity) {
        RetrieveSentMessagesResponseDto dto = new RetrieveSentMessagesResponseDto();
        dto.setNotificationId(String.valueOf(entity.getId()));
        dto.setSubject(entity.getSubject());
        dto.setMessage(entity.getDescription());
        dto.setRead(entity.isRead());
        if (entity.getReceiver() != null) {
            dto.setReceiverName(
                    entity.getReceiver().getFirstName() + " " + entity.getReceiver().getLastName()
            );
        }
        return dto;
    }

}
