package com.example.chatbot.service.ai.memory;

import com.example.chatbot.dto.globalMemory.RetrieveGlobalMemoryResponseDto;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.PersonGlobalMemoryEntity;
import com.example.chatbot.exception.ChatException;
import com.example.chatbot.exception.GlobalMemoryException;
import com.example.chatbot.repository.PersonGlobalMemoryRepository;
import com.example.chatbot.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonGlobalMemoryService {

    private final PersonGlobalMemoryRepository globalMemoryRepository;
    private final PersonRepository personRepository;

    private static final int GLOBAL_MEMORY_THRESHOLD = 8;

    @Transactional
    public void storeGlobalMemories(PersonEntity person, List<Map<String, Object>> extractedFacts) {

        log.info("Starting to store global memories for person {}, facts count: {}",
                person.getId(), extractedFacts.size());

        for (Map<String, Object> fact : extractedFacts) {
            String key = (String) fact.get("key");
            String value = (String) fact.get("value");
            Integer importance = parseImportance(fact.get("importance"));

            if (key == null || value == null || importance == null) {
                log.warn("Skipping invalid global fact: {}", fact);
                continue;
            }

            log.debug("Processing global fact - key: {}, value: {}, importance: {}", key, value, importance);

            if (importance >= GLOBAL_MEMORY_THRESHOLD) {

                PersonGlobalMemoryEntity memory = globalMemoryRepository
                        .findByPersonEntityAndKey(person, key).
                        orElseThrow(()->new GlobalMemoryException("It run into a problem trying to retrieve global memories"));

                boolean isNew = (memory == null);

                if (isNew) {
                    memory = new PersonGlobalMemoryEntity();
                    memory.setPersonEntity(person);
                    memory.setKey(key);
                    memory.setValue(value);
                    memory.setPriority(calculatePriority(importance, true));
                    memory.setFrequency(1);
                    log.info("Creating NEW global memory: {} = {} (priority: {})",
                            key, value, memory.getPriority());
                } else {
                    memory.setValue(value);
                    memory.setFrequency(memory.getFrequency() + 1);
                    memory.setPriority(calculatePriorityWithFrequency(importance, memory.getFrequency()));
                    log.info("Updating EXISTING global memory: {} = {} (priority: {}, frequency: {})",
                            key, value, memory.getPriority(), memory.getFrequency());
                }

                globalMemoryRepository.save(memory);
                log.info("Stored global memory for user {}: {} = {} (priority: {})",
                        person.getId(), key, value, memory.getPriority());
            } else {
                log.debug("Importance {} below threshold {} for key: {}",
                        importance, GLOBAL_MEMORY_THRESHOLD, key);
            }
        }
    }

    public List<PersonGlobalMemoryEntity> getTopGlobalMemories(PersonEntity person, int limit) {
        return globalMemoryRepository.findTopByPersonOrderByPriorityDesc(
                person,
                PageRequest.of(0, limit)
        ).orElseThrow(()->new GlobalMemoryException("It run into a problem trying to retrieve global memories"));
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

    // AGREGAR ESTE MÉTODO
    private Integer parseImportance(Object importanceObj) {
        if (importanceObj == null) {
            log.debug("Importance is null, using default value 5");
            return 5;
        }

        if (importanceObj instanceof Integer) {
            return (Integer) importanceObj;
        }

        if (importanceObj instanceof Double) {
            return ((Double) importanceObj).intValue();
        }

        if (importanceObj instanceof String) {
            try {
                return Integer.parseInt((String) importanceObj);
            } catch (NumberFormatException e) {
                log.warn("Invalid importance string value: {}, using default 5", importanceObj);
                return 5;
            }
        }

        if (importanceObj instanceof Number) {
            return ((Number) importanceObj).intValue();
        }

        log.warn("Unknown importance type: {}, using default 5", importanceObj.getClass());
        return 5;
    }

    public List<RetrieveGlobalMemoryResponseDto> RetrieveGlobalMemories(String personId) {
        PersonEntity person = personRepository.findById(UUID.fromString(personId))
                .orElseThrow(() -> new ChatException("It Run into a problem trying to find the person"));
        return globalMemoryRepository.findKeyValuesByPerson(person)
                .orElseThrow(()->new GlobalMemoryException("It run into a problem trying to retrieve global memories"));
    }

}
