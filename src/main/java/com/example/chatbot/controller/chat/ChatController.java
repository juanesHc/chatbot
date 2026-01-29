package com.example.chatbot.controller.chat;

import com.example.chatbot.dto.chat.request.RegisterChatNameRequestDto;
import com.example.chatbot.dto.chat.response.RegisterChatNameResponseDto;
import com.example.chatbot.service.chat.RegisterChatNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final RegisterChatNameService registerChatNameService;

    @PostMapping("/name")
    public ResponseEntity<RegisterChatNameResponseDto> postChatName(@RequestBody RegisterChatNameRequestDto
                                                                           registerChatNameRequestDto){
        RegisterChatNameResponseDto responseDto=registerChatNameService.registerChat(registerChatNameRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
