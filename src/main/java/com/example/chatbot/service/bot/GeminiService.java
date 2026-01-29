package com.example.chatbot.service.bot;

import com.example.chatbot.dto.chat.request.ChatBotRequestDto;
import com.example.chatbot.dto.chat.response.ChatBotResponseDto;
import com.example.chatbot.exception.GeminiException;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {


    private final Client client;
    private final MemoryService chatMemoryService;

    @Value("${genai.model}")
    private String gemini;

    private static final String PROMPT_REGEX = "^(?=\\s*\\S)(?=.{1,250}$).*";

    public ChatBotResponseDto askGemini(ChatBotRequestDto chatBotRequestDto) {
return null;
    }


    private ChatBotResponseDto generateGeminiResponse(String prompt) {
        return null;
    }


}
