package com.example.chatbot.mapper.person;

import com.example.chatbot.dto.user.request.RegisterUserRequestDto;
import com.example.chatbot.entity.PersonEntity;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonEntity registerGoogleRequestDtoToPersonEntity(RegisterUserRequestDto registerUserRequestDto){
        PersonEntity personEntity=new PersonEntity();

        personEntity.setEmail(registerUserRequestDto.getEmail());
        personEntity.setName(registerUserRequestDto.getName());

        return personEntity;
    }

}
