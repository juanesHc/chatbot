package com.example.chatbot.repository;

import com.example.chatbot.entity.ChatEntity;
import com.example.chatbot.entity.MemoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemoryRepository extends JpaRepository<MemoryEntity, UUID> {


    List<MemoryEntity> findByChatEntity(ChatEntity chat);

    @Query("SELECT m FROM MemoryEntity m WHERE m.chatEntity = :chat ORDER BY m.priority DESC LIMIT :limit")
    List<MemoryEntity> findTopByChatEntityOrderByPriorityDesc(ChatEntity chat, int limit);

}
