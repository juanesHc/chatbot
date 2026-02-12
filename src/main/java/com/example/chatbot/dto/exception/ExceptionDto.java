package com.example.chatbot.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionDto {
    private String code;
    private String message;
    private Map<String, String> errors;

    public ExceptionDto(String code, String message) {
        this.code = code;
        this.message = message;
        this.errors = null;
    }
}
