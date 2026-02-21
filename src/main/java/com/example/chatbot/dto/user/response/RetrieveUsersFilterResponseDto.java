package com.example.chatbot.dto.user.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrieveUsersFilterResponseDto {

    private String givenName;
    private String familyName;
    private String email;
    private String provider;
    private String roleName;
    private LocalDateTime createdAt;

}
