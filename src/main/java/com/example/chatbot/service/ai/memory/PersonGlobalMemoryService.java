package com.example.chatbot.service.ai.memory;

import com.example.chatbot.entity.MemoryEntity;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.PersonGlobalMemoryEntity;
import com.example.chatbot.repository.PersonGlobalMemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonGlobalMemoryService {

    private final PersonGlobalMemoryRepository globalMemoryRepository;

    private static final int GLOBAL_MEMORY_THRESHOLD = 95;

    @Transactional
    public void storeGlobalMemories(PersonEntity person, List<Map<String, Object>> extractedFacts) {

        for (Map<String, Object> fact : extractedFacts) {
            String key = (String) fact.get("key");
            String value = (String) fact.get("value");
            Integer importance = (Integer) fact.get("importance");

            if (importance >= 8) {

                PersonGlobalMemoryEntity memory = globalMemoryRepository
                        .findByPersonAndKey(person, key);

                boolean isNew = memory.getId()== null;

                memory.setKey(key);
                memory.setValue(value);
                memory.setPerson(person);

                if (isNew) {
                    memory.setPriority(calculatePriority(importance, true));
                    memory.setFrequency(1);
                } else {
                    memory.setFrequency(memory.getFrequency() + 1);
                    memory.setPriority(calculatePriorityWithFrequency(importance, memory.getFrequency()));
                }

                globalMemoryRepository.save(memory);
                log.info("Stored global memory for user {}: {} = {}", person.getId(), key, value);
            }
        }
    }

    @Transactional
    public void promoteToGlobal(PersonEntity person, MemoryEntity chatMemory) {

        if (chatMemory.getPriority() >= GLOBAL_MEMORY_THRESHOLD) {

            PersonGlobalMemoryEntity globalMemory = globalMemoryRepository
                    .findByPersonAndKey(person, chatMemory.getKey());

            globalMemory.setKey(chatMemory.getKey());
            globalMemory.setValue(chatMemory.getValue());
            globalMemory.setPerson(person);
            globalMemory.setPriority(chatMemory.getPriority());

            if (globalMemory.getFrequency() == null) {
                globalMemory.setFrequency(1);
            } else {
                globalMemory.setFrequency(globalMemory.getFrequency() + 1);
            }

            globalMemoryRepository.save(globalMemory);
            log.info("Promoted memory to global: {} = {}", chatMemory.getKey(), chatMemory.getValue());
        }
    }

    public List<PersonGlobalMemoryEntity> getTopGlobalMemories(PersonEntity person, int limit) {
        // Usar PageRequest para limitar resultados
        return globalMemoryRepository.findTopByPersonOrderByPriorityDesc(
                person,
                PageRequest.of(0, limit) // Página 0, con 'limit' elementos
        );
    }



    public List<PersonGlobalMemoryEntity> getHighPriorityMemories(PersonEntity person) {
        return globalMemoryRepository.findHighPriorityMemories(person, GLOBAL_MEMORY_THRESHOLD);
    }

    private Integer calculatePriority(Integer importance, boolean isNew) {
        if (importance == null || importance < 1) {
            importance = 1;
        }

        int priority = 10 + (importance * 8);

        if (isNew) {
            priority = Math.min(priority + 10, 100);
        }

        return priority;
    }


    private Integer calculatePriorityWithFrequency(Integer importance, Integer frequency) {
        int basePriority = 10 + (importance * 8);

        int frequencyBoost = Math.min(frequency * 2, 20);

        return Math.min(basePriority + frequencyBoost, 100);
    }

}
