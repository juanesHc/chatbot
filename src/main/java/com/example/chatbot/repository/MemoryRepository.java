package com.example.chatbot.repository;

import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MemoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemoryRepository extends JpaRepository<MemoryEntity, UUID> {


    Optional<List<MemoryEntity>> findByChatEntity(ChatEntity chat);

    void deleteAllByChatEntity(ChatEntity chatEntity);

    @Query("SELECT m FROM MemoryEntity m WHERE m.chatEntity = :chat ORDER BY m.priority DESC LIMIT :limit")
    Optional<List<MemoryEntity>> findTopByChatEntityOrderByPriorityDesc(ChatEntity chat, int limit);

    Optional<MemoryEntity> findByChatEntityAndKey(ChatEntity chat, String key);

}
