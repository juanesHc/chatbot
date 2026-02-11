package com.example.chatbot.controller.memory;

import com.example.chatbot.dto.globalMemory.RetrieveGlobalMemoryResponseDto;
import com.example.chatbot.service.ai.memory.PersonGlobalMemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/memories")
@RestController
public class MemoryController {

    private final PersonGlobalMemoryService personGlobalMemoryService;

    @GetMapping("{personId}/retrieve")
    public ResponseEntity<List<RetrieveGlobalMemoryResponseDto>> getMemories(@PathVariable String personId){
        return ResponseEntity.ok(personGlobalMemoryService.RetrieveGlobalMemories(personId));
    }


}
