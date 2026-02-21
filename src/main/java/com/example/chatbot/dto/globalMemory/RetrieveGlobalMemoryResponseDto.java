package com.example.chatbot.dto.globalMemory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RetrieveGlobalMemoryResponseDto {
    private String id;
    private String key;
    private String value;

    public RetrieveGlobalMemoryResponseDto(UUID id, String key, String value) {
        this.id = id.toString();
        this.key = key;
        this.value = value;
    }

}
