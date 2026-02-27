package com.example.chatbot.mapper.person;

import com.example.chatbot.dto.user.request.RegisterUserRequestDto;
import com.example.chatbot.dto.user.request.RegisterUserWithRoleRequestDto;
import com.example.chatbot.dto.user.response.RetrievePersonDataResponseDto;
import com.example.chatbot.dto.user.response.RetrieveUsersFilterResponseDto;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PersonMapper {

    public PersonEntity registerGoogleRequestDtoToPersonEntity(RegisterUserRequestDto registerUserRequestDto){
        PersonEntity personEntity=new PersonEntity();

        personEntity.setEmail(registerUserRequestDto.getEmail());
        personEntity.setFirstName(registerUserRequestDto.getFirstName());
        personEntity.setLastName(registerUserRequestDto.getLastName());

        return personEntity;
    }

    public RetrieveUsersFilterResponseDto PersonEntityToRetrieveUsersFilterDto(PersonEntity entity) {
        return RetrieveUsersFilterResponseDto.builder()
                .givenName(entity.getFirstName())
                .familyName(entity.getLastName())
                .email(entity.getEmail())
                .roleName(String.valueOf(entity.getRoleEntity().getType()))
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public PersonEntity registerWithRoleDtoToPersonEntity(RegisterUserWithRoleRequestDto requestDto){
        PersonEntity personEntity=new PersonEntity();

        personEntity.setEmail(requestDto.getEmail());
        personEntity.setFirstName(requestDto.getFirstName());
        personEntity.setLastName(requestDto.getLastName());

        return personEntity;
    }

    public RetrievePersonDataResponseDto personEntityToRetrievePersonDataResponseDto(PersonEntity personEntity){
        RetrievePersonDataResponseDto retrievePersonDataResponseDto=new RetrievePersonDataResponseDto();
        retrievePersonDataResponseDto.setLastName(personEntity.getLastName());
        retrievePersonDataResponseDto.setFirstName(personEntity.getFirstName());
        retrievePersonDataResponseDto.setEmail(personEntity.getEmail());

        LocalDate lastUpdate= LocalDate.from(personEntity.getUpdatedAt());
        retrievePersonDataResponseDto.setLastUpdate(String.valueOf(lastUpdate));

        return retrievePersonDataResponseDto;
    }


}
