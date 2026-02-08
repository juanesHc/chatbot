package com.example.chatbot.repository;

import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    List<MessageEntity> findByChatEntity(ChatEntity chatEntity);

}
