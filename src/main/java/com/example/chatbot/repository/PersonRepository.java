package com.example.chatbot.repository;

import com.example.chatbot.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, UUID> , JpaSpecificationExecutor<PersonEntity> {

    Optional<PersonEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

}
