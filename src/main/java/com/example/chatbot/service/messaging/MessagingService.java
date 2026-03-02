package com.example.chatbot.service.messaging;

import com.example.chatbot.dto.notification.request.RegisterInternalNotificationRequestDto;
import com.example.chatbot.dto.notification.request.RegisterNotificationRequestDto;
import com.example.chatbot.dto.notification.request.RetrieveNotificationRequestDto;
import com.example.chatbot.dto.notification.response.EliminationNotificationResponseDto;
import com.example.chatbot.dto.notification.response.RegisterAdminNotificationResponseDto;
import com.example.chatbot.dto.notification.response.RetrieveMyNotificationResponseDto;
import com.example.chatbot.dto.notification.response.RetrieveSentMessagesResponseDto;
import com.example.chatbot.entity.NotificationEntity;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.enums.PersonNotificationRole;
import com.example.chatbot.exception.EliminationNotification;
import com.example.chatbot.exception.RegisterNotificationException;
import com.example.chatbot.exception.RetrieveNotificationException;
import com.example.chatbot.exception.RetrievePersonException;
import com.example.chatbot.mapper.notification.NotificationMapper;
import com.example.chatbot.repository.NotificationRepository;
import com.example.chatbot.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final NotificationRepository notificationRepository;
    private final PersonRepository personRepository;
    private final NotificationMapper notificationMapper;


    public List<RetrieveMyNotificationResponseDto> retrieveMyMessages(String personId, RetrieveNotificationRequestDto requestDto) {
        List<NotificationEntity> notificationEntities = notificationRepository
                .findByReceiverId(UUID.fromString(personId))
                .orElseThrow(() -> new RetrieveNotificationException("It run into a problem retrieving notifications"));

        Stream<NotificationEntity> stream = notificationEntities.stream();

        if (requestDto.getRead() != null) {
            stream = stream.filter(e -> e.isRead() == requestDto.getRead());
        }

        if (requestDto.getRole() != null && !requestDto.getRole().isBlank()) {
            PersonNotificationRole role = PersonNotificationRole.valueOf(requestDto.getRole());
            stream = stream.filter(e -> e.getPersonNotificationRole() == role);
        }

        return stream
                .map(entity -> notificationMapper.notificationEntityToRetrieveNotificationResponseDto(entity))
                .collect(Collectors.toList());
    }

    public void registerInternalNotification(String personId, RegisterInternalNotificationRequestDto requestDto){
        PersonEntity personEntity=personRepository.findById(UUID.fromString(personId)).
                orElseThrow(()-> new RegisterNotificationException("It run into a problem sending the message"));

        NotificationEntity notificationEntity=notificationMapper.registerInternalNotificationRequestDtoToEntity(personEntity,requestDto);
        notificationEntity.setPersonNotificationRole(PersonNotificationRole.SYSTEM);
        notificationRepository.save(notificationEntity);
    }

    public RegisterAdminNotificationResponseDto registerAdminNotification(String personId, RegisterNotificationRequestDto requestDto){
        registerNotification(personId,requestDto);
        return new RegisterAdminNotificationResponseDto("Message Successfully sent");
    }

    public EliminationNotificationResponseDto dropNotification(String notificationId){
        NotificationEntity notificationEntity=notificationRepository.findById(UUID.fromString(notificationId)).
                orElseThrow(()->new EliminationNotification("It run into a problem trying to eliminate the notification"));

        notificationRepository.delete(notificationEntity);
        return new EliminationNotificationResponseDto("Notification successfully dropped");
    }

    public List<RetrieveSentMessagesResponseDto> retrieveAdminSentMessages(String adminId) {
        List<NotificationEntity> notificationEntitiesList = notificationRepository
                .findBySenderIdAndPersonNotificationRole(
                        UUID.fromString(adminId),
                        PersonNotificationRole.RECEIVER)
                .orElseThrow(() -> new RetrievePersonException("Could not found the person"));

        return notificationEntitiesList.stream()
                .map(notificationMapper::mapToSentMessageDto)
                .collect(Collectors.toList());
    }

    public void markNotificationAsRead(String notificationId){
        NotificationEntity notification = notificationRepository.findById(UUID.fromString(notificationId))
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    private void registerNotification(
            String personId,
            RegisterNotificationRequestDto requestDto){
        PersonEntity personEntitySender=personRepository.findById(UUID.fromString(requestDto.getSenderId())).
                orElseThrow(()-> new RegisterNotificationException("It run into a problem sending the message"));
        saveNotification(personEntitySender,PersonNotificationRole.SENDER,requestDto);
        log.info("Message sent in a successful way");

        PersonEntity personEntityReceiver=personRepository.findById(UUID.fromString(personId)).
                orElseThrow(()-> new RegisterNotificationException("It run into a problem sending the message"));
        saveNotification(personEntityReceiver,PersonNotificationRole.RECEIVER,requestDto);

        log.info ("Message successfully sent");
    }

    private void saveNotification(PersonEntity personEntity, PersonNotificationRole personNotificationRole, RegisterNotificationRequestDto requestDto) {
        NotificationEntity notificationEntity = notificationMapper.registerNotificationRequestDtoToEntity(personEntity, requestDto);
        notificationEntity.setPersonNotificationRole(personNotificationRole);

        PersonEntity sender = personRepository.findById(UUID.fromString(requestDto.getSenderId()))
                .orElseThrow(() -> new RegisterNotificationException("Sender not found"));
        notificationEntity.setSender(sender);

        notificationEntity.setReceiver(personEntity);

        notificationRepository.save(notificationEntity);
    }




}
