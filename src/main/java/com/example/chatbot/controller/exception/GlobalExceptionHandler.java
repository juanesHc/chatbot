package com.example.chatbot.controller.exception;

import com.example.chatbot.dto.exception.ExceptionDto;
import com.example.chatbot.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ExceptionDto("VALIDATION_ERROR", "Invalid input", errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return new ExceptionDto("INTERNAL_ERROR", "An unexpected error occurred", null);
    }

    @ExceptionHandler(ChatException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleChat(ChatException ex) {
        log.error("Chat error", ex);
        return new ExceptionDto("CHAT_ERROR", ex.getMessage(), null);
    }

    @ExceptionHandler(GeminiException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleGemini(GeminiException ex) {
        log.error("Gemini error", ex);
        return new ExceptionDto("GEMINI_ERROR", ex.getMessage(), null);
    }

    @ExceptionHandler(MessageException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleMessage(MessageException ex) {
        log.error("Message error", ex);
        return new ExceptionDto("MESSAGE_ERROR", ex.getMessage(), null);
    }

    @ExceptionHandler(RegisterChatNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleRegisterChatName(RegisterChatNameException ex) {
        log.error("Register ChatName error", ex);
        return new ExceptionDto("REGISTER_CHAT_NAME_ERROR", ex.getMessage(), null);
    }
    @ExceptionHandler(RegisterUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleRegisterUser(RegisterUserException ex) {
        log.error("Register user error", ex);
        return new ExceptionDto("REGISTER_USER_ERROR", ex.getMessage(), null);
    }

    @ExceptionHandler(MemoryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleRegisterUser(MemoryException ex) {
        log.error("Delete memory error", ex);
        return new ExceptionDto("DELETE_MEMORY_ERROR", ex.getMessage(), null);
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleLogin(LoginException ex) {
        log.error("Login error", ex);
        return new ExceptionDto("LOGIN_ERROR", ex.getMessage(), null);
    }

}
