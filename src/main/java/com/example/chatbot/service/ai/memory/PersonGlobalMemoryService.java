package com.example.chatbot.service.ai.memory;

import com.example.chatbot.dto.globalMemory.RetrieveGlobalMemoryResponseDto;
import com.example.chatbot.entity.MemoryEntity;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.PersonGlobalMemoryEntity;
import com.example.chatbot.exception.ChatException;
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

    private static final int GLOBAL_MEMORY_THRESHOLD = 95;

    @Transactional
    public void storeGlobalMemories(PersonEntity person, List<Map<String, Object>> extractedFacts) {

        for (Map<String, Object> fact : extractedFacts) {
            String key = (String) fact.get("key");
            String value = (String) fact.get("value");
            Integer importance = (Integer) fact.get("importance");

            if (importance >= 95) {

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


    public List<PersonGlobalMemoryEntity> getTopGlobalMemories(PersonEntity person, int limit) {
        return globalMemoryRepository.findTopByPersonOrderByPriorityDesc(
                person,
                PageRequest.of(0, limit)
        );
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

    public List<RetrieveGlobalMemoryResponseDto> RetrieveGlobalMemories(String personId){

        PersonEntity person=personRepository.findById(UUID.fromString(personId)).
                orElseThrow(()->new ChatException("It Run into a problem trying to find the person"));
        return globalMemoryRepository.findKeyValuesByPerson(person);
    }

}
