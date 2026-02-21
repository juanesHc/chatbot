package com.example.chatbot.repository;

import com.example.chatbot.entity.RoleEntity;
import com.example.chatbot.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByType(RoleEnum type);

}
