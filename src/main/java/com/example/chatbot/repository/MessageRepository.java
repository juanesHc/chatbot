package com.example.chatbot.repository;

import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MemoryEntity;
import com.example.chatbot.entity.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    void deleteAllByChatEntity(ChatEntity chatEntity);

    @Query("SELECT m FROM MessageEntity m WHERE m.chatEntity.id = :chatId ORDER BY m.createdAt ASC")
    Optional<List<MessageEntity>> findByChatIdOrderByTimestampAsc(@Param("chatId") UUID chatId);

    Optional<List<MessageEntity>> findTopByChatEntityOrderByCreatedAtDesc(ChatEntity chat, Pageable pageable);

}
