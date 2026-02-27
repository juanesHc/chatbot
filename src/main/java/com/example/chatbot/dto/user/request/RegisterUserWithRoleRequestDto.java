package com.example.chatbot.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserWithRoleRequestDto {
    private String email;
    private String firstName;
    private String lastName;
    private String roleName;
}
