package com.example.chatbot.controller.role;

import com.example.chatbot.dto.role.RetrieveRolesResponseDto;
import com.example.chatbot.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/retrieve")
    public ResponseEntity<RetrieveRolesResponseDto> getRoles(){
            return ResponseEntity.ok(roleService.retrieveRoles());
    }
}
