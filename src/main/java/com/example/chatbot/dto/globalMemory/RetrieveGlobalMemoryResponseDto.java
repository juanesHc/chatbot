package com.example.chatbot.dto.globalMemory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RetrieveGlobalMemoryResponseDto {
    private String key;
    private String value;
}
