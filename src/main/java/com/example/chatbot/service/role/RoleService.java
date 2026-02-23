package com.example.chatbot.service.role;

import com.example.chatbot.dto.role.RetrieveRolesResponseDto;
import com.example.chatbot.entity.enums.RoleEnum;
import com.example.chatbot.exception.RetrieveRolesException;
import com.example.chatbot.mapper.role.RoleMapper;
import com.example.chatbot.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RetrieveRolesResponseDto retrieveRoles(){
    List<RoleEnum> roleEnumList= roleRepository.findAllTypes().
            orElseThrow(()->new RetrieveRolesException("It run into a problem trying to retrieve roles"));

    return roleMapper.roleEnumToRetrieveRoleResponse(roleEnumList);
    }



}
