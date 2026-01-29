package com.example.chatbot.controller.chat;

import com.example.chatbot.dto.chat.request.RegisterChatNameRequestDto;
import com.example.chatbot.dto.chat.response.RegisterChatNameResponseDto;
import com.example.chatbot.dto.chat.response.RetrieveChatsNameResponseDto;
import com.example.chatbot.service.chat.RegisterChatNameService;
import com.example.chatbot.service.chat.RetrieveChatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatBotController {

    private final RegisterChatNameService registerChatNameService;
    private final RetrieveChatsService retrieveChatsService;

    @PostMapping("/{personId}/register/name")
    public ResponseEntity<RegisterChatNameResponseDto> postChatName(@RequestBody RegisterChatNameRequestDto
                                                                           registerChatNameRequestDto,@PathVariable String personId){
        RegisterChatNameResponseDto responseDto=registerChatNameService.registerChat(registerChatNameRequestDto,personId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("{personId}/retrieve/names")
    public ResponseEntity<List<RetrieveChatsNameResponseDto>> getChatsName(@PathVariable String personId){
        List<RetrieveChatsNameResponseDto> responseDto=retrieveChatsService.retrieveChatsName(personId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
