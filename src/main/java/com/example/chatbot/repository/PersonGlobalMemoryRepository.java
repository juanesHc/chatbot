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
import java.util.UUID;

@Repository
public interface PersonGlobalMemoryRepository extends JpaRepository<PersonGlobalMemoryEntity, UUID> {

    PersonGlobalMemoryEntity findByPersonAndKey(PersonEntity person, String key);

    @Query("SELECT m FROM PersonGlobalMemoryEntity m WHERE m.person = :person ORDER BY m.priority DESC")
    List<PersonGlobalMemoryEntity> findTopByPersonOrderByPriorityDesc(PersonEntity person, Pageable pageable);

    @Query("SELECT new com.example.chatbot.dto.globalMemory.RetrieveGlobalMemoryResponseDto(m.id,m.key, m.value) " +
            "FROM PersonGlobalMemoryEntity m WHERE m.person = :person")
    List<RetrieveGlobalMemoryResponseDto> findKeyValuesByPerson(@Param("person") PersonEntity person);

}
