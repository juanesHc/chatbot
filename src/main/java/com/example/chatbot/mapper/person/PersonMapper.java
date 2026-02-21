package com.example.chatbot.mapper.person;

import com.example.chatbot.dto.user.request.RegisterUserRequestDto;
import com.example.chatbot.dto.user.response.RetrieveUsersFilterResponseDto;
import com.example.chatbot.entity.PersonEntity;
import org.springframework.stereotype.Component;

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


}
