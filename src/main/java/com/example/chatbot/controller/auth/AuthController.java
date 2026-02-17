package com.example.chatbot.controller.auth;

import com.example.chatbot.dto.login.response.LoginResponseDto;
import com.example.chatbot.dto.user.request.RegisterUserRequestDto;
import com.example.chatbot.service.person.user.RegisterUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {
    private final RegisterUserService registerUserService;

    @PostMapping("/google")
    public ResponseEntity<LoginResponseDto> loginWithGoogle(
            @RequestBody RegisterUserRequestDto request
    ) {
        LoginResponseDto response = registerUserService.registerByGoogleProvider(request);
        return ResponseEntity.ok(response);
    }
}
