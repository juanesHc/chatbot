package com.example.chatbot.dto.user.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RetrievePersonDataResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String lastUpdate;
}
