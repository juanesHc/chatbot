package com.example.chatbot.mapper.notification;

import com.example.chatbot.dto.notification.request.RegisterNotificationRequestDto;
import com.example.chatbot.dto.notification.response.RetrieveMyNotificationResponseDto;
import com.example.chatbot.entity.NotificationEntity;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.enums.NotificationTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationEntity registerNotificationRequestDtoToEntity(PersonEntity personEntity,
                                                                     NotificationTypeEnum notificationTypeEnum,
                                                                     RegisterNotificationRequestDto requestDto){
        NotificationEntity notificationEntity=new NotificationEntity();
        notificationEntity.setPersonEntity(personEntity);
        notificationEntity.setNotificationTypeEnum(notificationTypeEnum);
        notificationEntity.setDescription(requestDto.getMessageDescription());
        notificationEntity.setRead(false);

        return notificationEntity;
    }

    public RetrieveMyNotificationResponseDto notificationEntityToRetrieveNotificationResponseDto(NotificationEntity notificationEntity){

        RetrieveMyNotificationResponseDto retrieveMyNotificationResponseDto=new RetrieveMyNotificationResponseDto();
        retrieveMyNotificationResponseDto.setMessage(notificationEntity.getDescription());
        retrieveMyNotificationResponseDto.setNotificationId(String.valueOf(notificationEntity.getId()));

        return retrieveMyNotificationResponseDto;

    }

}
