package com.example.chatbot.dto.user.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedUsersResponseDto {
    private List<RetrieveUsersFilterResponseDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
