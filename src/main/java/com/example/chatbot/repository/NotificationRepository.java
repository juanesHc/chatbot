package com.example.chatbot.repository;

import com.example.chatbot.entity.NotificationEntity;
import com.example.chatbot.entity.enums.PersonNotificationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    Optional<List<NotificationEntity>> findByReceiverId(UUID id);

    Optional<List<NotificationEntity>> findBySenderIdAndPersonNotificationRole(UUID senderId, PersonNotificationRole role);
}
