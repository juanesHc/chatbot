package com.example.chatbot.controller.memory;

import com.example.chatbot.dto.globalMemory.DeleteGlobalMemoryResponseDto;
import com.example.chatbot.dto.globalMemory.RetrieveGlobalMemoryResponseDto;
import com.example.chatbot.service.ai.memory.MemoryService;
import com.example.chatbot.service.ai.memory.PersonGlobalMemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/memories")
@RestController
public class MemoryController {

    private final PersonGlobalMemoryService personGlobalMemoryService;
    private final MemoryService memoryService;

    @GetMapping("{personId}/retrieve")
    public ResponseEntity<List<RetrieveGlobalMemoryResponseDto>> getMemories(@PathVariable String personId){
        return ResponseEntity.ok(personGlobalMemoryService.RetrieveGlobalMemories(personId));
    }

    @DeleteMapping("delete/{memoryId}")
    public ResponseEntity<DeleteGlobalMemoryResponseDto> deleteGlobalMemories(@PathVariable String memoryId){
        return ResponseEntity.ok(memoryService.deleteMemory(memoryId));
    }

}
