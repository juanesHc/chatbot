package com.example.chatbot.service.person.admin;

import com.example.chatbot.dto.user.request.RegisterUserWithRoleRequestDto;
import com.example.chatbot.dto.user.response.RegisterUserWithRoleResponseDto;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.RoleEntity;
import com.example.chatbot.entity.enums.RoleEnum;
import com.example.chatbot.exception.RegisterUserException;
import com.example.chatbot.mapper.person.PersonMapper;
import com.example.chatbot.repository.PersonRepository;
import com.example.chatbot.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserWithRoleService {

    private final PersonMapper personMapper;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    public RegisterUserWithRoleResponseDto registerUserWithRole(RegisterUserWithRoleRequestDto requestDto){
        if(isEmailUnique(requestDto.getEmail())){
            throw new RegisterUserException("Email is already in use");
        }

        PersonEntity personEntity=personMapper.registerWithRoleDtoToPersonEntity(requestDto);

        RoleEntity roleEntity=roleRepository.findByType(RoleEnum.valueOf(requestDto.getRoleName())).
                orElseThrow(()->new RegisterUserException("It run into a problem retrieving roles"));

        personEntity.setRoleEntity(roleEntity);
        personRepository.save(personEntity);

        return new RegisterUserWithRoleResponseDto("The user was successfully saved");
    }

    private Boolean isEmailUnique(String email){
        return personRepository.existsByEmail(email);
    }

}
