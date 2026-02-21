package com.example.chatbot.controller.person;

import com.example.chatbot.dto.user.request.RetrieveUsersFilterRequestDto;
import com.example.chatbot.dto.user.response.PagedUsersResponseDto;
import com.example.chatbot.service.person.admin.RetrieveUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class PersonController {

    private final RetrieveUsersService userService;

    @GetMapping("/retrieve")
    public ResponseEntity<PagedUsersResponseDto> retrieveUsers(
            @RequestParam(required = false) String givenName,
            @RequestParam(required = false) String familyName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sourceDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        RetrieveUsersFilterRequestDto filter = new RetrieveUsersFilterRequestDto(
                givenName, familyName, email, provider, roleName, createdAt, targetDate, sourceDate
        );
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return ResponseEntity.ok(
                userService.retrieveUsers(filter, PageRequest.of(page, size, sort))
        );
    }

}
