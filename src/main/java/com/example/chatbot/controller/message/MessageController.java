package com.example.chatbot.controller.message;

import com.example.chatbot.dto.chat.response.ChatBotResponseDto;
import com.example.chatbot.dto.message.RegisterMessageRequestDto;
import com.example.chatbot.dto.message.RetrieveMessageResponseDto;
import com.example.chatbot.service.ai.gemini.GeminiService;
import com.example.chatbot.service.ai.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/message")
@RequiredArgsConstructor
public class MessageController {

    private final GeminiService geminiService;
    private final MessageService messageService;


    @PostMapping("{chatId}/ask")
    public ResponseEntity<ChatBotResponseDto> askChatBot(@RequestBody RegisterMessageRequestDto registerMessageRequestDto, @PathVariable String chatId){
        return ResponseEntity.ok(geminiService.askGemini(registerMessageRequestDto,chatId));
    }


    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<RetrieveMessageResponseDto>> getMessages(@PathVariable String chatId) {
        List<RetrieveMessageResponseDto> messages = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }

}
