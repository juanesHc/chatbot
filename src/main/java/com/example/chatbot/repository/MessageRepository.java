package com.example.chatbot.repository;

import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MemoryEntity;
import com.example.chatbot.entity.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    long countByChatEntity(ChatEntity chatEntity);

    void deleteAllByChatEntity(ChatEntity chatEntity);

    List<MessageEntity> findByChatEntity(ChatEntity chat);

    List<MessageEntity> findTopByChatEntityOrderByCreatedAtDesc(ChatEntity chat, Pageable pageable);

}
