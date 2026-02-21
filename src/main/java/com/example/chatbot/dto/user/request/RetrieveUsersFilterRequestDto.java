package com.example.chatbot.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveUsersFilterRequestDto {

    private String givenName;
    private String familyName;
    private String email;
    private String provider;
    private String roleName;
    private LocalDate createdAt;
    private LocalDate targetDate;
    private LocalDate sourceDate;

}
