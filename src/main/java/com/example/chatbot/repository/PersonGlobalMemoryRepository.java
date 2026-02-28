package com.example.chatbot.repository;

import com.example.chatbot.dto.globalMemory.RetrieveGlobalMemoryResponseDto;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.PersonGlobalMemoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonGlobalMemoryRepository extends JpaRepository<PersonGlobalMemoryEntity, UUID> {

    Optional<PersonGlobalMemoryEntity> findByPersonEntityAndKey(PersonEntity personEntity, String key);

    @Query("SELECT m FROM PersonGlobalMemoryEntity m WHERE m.personEntity = :person ORDER BY m.priority DESC")
    Optional<List<PersonGlobalMemoryEntity>> findTopByPersonOrderByPriorityDesc(@Param("person") PersonEntity person, Pageable pageable);

    @Query("SELECT new com.example.chatbot.dto.globalMemory.RetrieveGlobalMemoryResponseDto(m.id, m.key, m.value) " +
            "FROM PersonGlobalMemoryEntity m WHERE m.personEntity = :person")
    Optional<List<RetrieveGlobalMemoryResponseDto>> findKeyValuesByPerson(@Param("person") PersonEntity person);

}
