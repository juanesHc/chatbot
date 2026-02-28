package com.example.chatbot.repository;

import com.example.chatbot.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    Optional<List<NotificationEntity>> findByPersonEntityId(UUID id);

}
