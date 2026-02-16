package com.example.chatbot.repository;

import com.example.chatbot.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {

    List<ChatEntity> findByPersonEntityId(UUID personId);

    Boolean existsByName(String name);


}
