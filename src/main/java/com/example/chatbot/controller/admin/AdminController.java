package com.example.chatbot.controller.admin;

import com.example.chatbot.dto.user.request.RegisterUserWithRoleRequestDto;
import com.example.chatbot.dto.user.request.RetrieveUsersFilterRequestDto;
import com.example.chatbot.dto.user.response.PagedUsersResponseDto;
import com.example.chatbot.dto.user.response.RegisterUserWithRoleResponseDto;
import com.example.chatbot.service.person.admin.RegisterUserWithRoleService;
import com.example.chatbot.service.person.admin.RetrieveUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AdminController {

    private final RetrieveUsersService userService;
    private final RegisterUserWithRoleService registerUserWithRoleService;

    @GetMapping("/retrieve")
    public ResponseEntity<PagedUsersResponseDto> retrieveUsers(
            @RequestParam(required = false) String givenName,
            @RequestParam(required = false) String familyName,
            @RequestParam(required = false) String email,
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
                givenName, familyName, email,  roleName, createdAt, targetDate, sourceDate
        );
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return ResponseEntity.ok(
                userService.retrieveUsers(filter, PageRequest.of(page, size, sort))
        );
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserWithRoleResponseDto> postUserByAdmin(@RequestBody RegisterUserWithRoleRequestDto requestDto){

        return ResponseEntity.ok(registerUserWithRoleService.registerUserWithRole(requestDto));


    }

}
