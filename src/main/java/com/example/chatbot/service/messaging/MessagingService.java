package com.example.chatbot.service.messaging;

import com.example.chatbot.dto.notification.request.RegisterNotificationRequestDto;
import com.example.chatbot.dto.notification.response.EliminationNotificationResponseDto;
import com.example.chatbot.dto.notification.response.RegisterAdminNotificationResponseDto;
import com.example.chatbot.dto.notification.response.RetrieveMyNotificationResponseDto;
import com.example.chatbot.entity.NotificationEntity;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.exception.EliminationNotification;
import com.example.chatbot.exception.RegisterNotificationException;
import com.example.chatbot.exception.RetrieveNotificationException;
import com.example.chatbot.mapper.notification.NotificationMapper;
import com.example.chatbot.repository.NotificationRepository;
import com.example.chatbot.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final NotificationRepository notificationRepository;
    private final PersonRepository personRepository;
    private final NotificationMapper notificationMapper;


    public List<RetrieveMyNotificationResponseDto> retrieveMyMessages(String personId){
        List<NotificationEntity> notificationEntities=notificationRepository.findByPersonEntityId(UUID.fromString(personId)).
                orElseThrow(()->new RetrieveNotificationException("It run into a problem retrieving notifications" ));

        List<RetrieveMyNotificationResponseDto> responseDtos=new ArrayList<>();

        notificationEntities.forEach(entity->responseDtos.add(notificationMapper.
                notificationEntityToRetrieveNotificationResponseDto(entity))
        );

        return responseDtos;

    }

    public void registerInternalNotification(String personId,RegisterNotificationRequestDto requestDto){
        registerNotification(personId,requestDto);
    }

    public RegisterAdminNotificationResponseDto registerAdminNotification(String personId, RegisterNotificationRequestDto requestDto){
        registerNotification(personId,requestDto);
        return new RegisterAdminNotificationResponseDto("Message Successfully sent");
    }

    private void registerNotification(
                                     String personId,
                                     RegisterNotificationRequestDto requestDto){

        PersonEntity personEntity=personRepository.findById(UUID.fromString(personId)).
                orElseThrow(()-> new RegisterNotificationException("It run into a problem sending the message"));

        NotificationEntity notificationEntity=notificationMapper.registerNotificationRequestDtoToEntity(personEntity,requestDto);
        notificationRepository.save(notificationEntity);
        log.info ("Message successfully sent");


    }

    public EliminationNotificationResponseDto dropNotification(String notificationId){
        NotificationEntity notificationEntity=notificationRepository.findById(UUID.fromString(notificationId)).
                orElseThrow(()->new EliminationNotification("It run into a problem trying to eliminate the notification"));

        notificationRepository.delete(notificationEntity);
        return new EliminationNotificationResponseDto("Notification successfully dropped");
    }

    public void markNotificationAsRead(String notificationId){
        NotificationEntity notification = notificationRepository.findById(UUID.fromString(notificationId))
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }



}
