package com.example.chatbot.dto.chat.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RegisterChatNameRequestDto {


    @NotBlank(message = "Chat name cannot be blank")
    @Size(min = 3, max = 50, message = "Chat name must be between 3 and 50 characters")
    private String name;
}
