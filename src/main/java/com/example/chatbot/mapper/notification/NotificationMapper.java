package com.example.chatbot.mapper.notification;

import com.example.chatbot.dto.notification.request.RegisterInternalNotificationRequestDto;
import com.example.chatbot.dto.notification.request.RegisterNotificationRequestDto;
import com.example.chatbot.dto.notification.request.RetrieveNotificationRequestDto;
import com.example.chatbot.dto.notification.response.RetrieveMyNotificationResponseDto;
import com.example.chatbot.entity.NotificationEntity;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.enums.NotificationTypeEnum;
import com.example.chatbot.exception.RetrieveNotificationException;
import com.example.chatbot.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    public NotificationEntity registerNotificationRequestDtoToEntity(PersonEntity personEntity,
                                                                     RegisterNotificationRequestDto requestDto){
        NotificationEntity notificationEntity=new NotificationEntity();
        notificationEntity.setPersonEntity(personEntity);
        notificationEntity.setSubject(requestDto.getSubject());
        notificationEntity.setDescription(requestDto.getMessageDescription());
        notificationEntity.setRead(false);

        return notificationEntity;
    }

    public NotificationEntity registerInternalNotificationRequestDtoToEntity(PersonEntity personEntity,
                                                                     RegisterInternalNotificationRequestDto requestDto){
        NotificationEntity notificationEntity=new NotificationEntity();
        notificationEntity.setPersonEntity(personEntity);
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

}
