package com.example.chatbot.service.person.user;

import com.example.chatbot.dto.login.response.LoginResponseDto;
import com.example.chatbot.dto.user.request.RegisterUserRequestDto;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.model.SecurityUser;
import com.example.chatbot.exception.RegisterUserException;
import com.example.chatbot.mapper.person.PersonMapper;
import com.example.chatbot.repository.PersonRepository;
import com.example.chatbot.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService {

    private final JwtService jwtService;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;


    public LoginResponseDto registerByGoogleProvider(RegisterUserRequestDto OAuth2GoogleRequestDto){
        PersonEntity personEntity = personRepository.findByEmail(OAuth2GoogleRequestDto.getEmail());

        if(validateEmailIsUnique((personEntity.getEmail()))){
            throw new RegisterUserException("Email is already in use");
        }

        if (personEntity==(null)) {
            return createGoogleUser(OAuth2GoogleRequestDto);
        }
        else {
            return generateTokenGoogle(personEntity);
        }

    }

    private LoginResponseDto createGoogleUser(RegisterUserRequestDto OAuth2GoogleRequestDto) {

        PersonEntity newUser = personMapper.registerGoogleRequestDtoToPersonEntity(OAuth2GoogleRequestDto);
        personRepository.save(newUser);

        return generateTokenGoogle(newUser);

    }


    private LoginResponseDto generateTokenGoogle(PersonEntity personEntity) {

        UserDetails userDetails = new SecurityUser(personEntity);

        String token = jwtService.generateToken(
                userDetails,
                personEntity.getId(),
                personEntity.getName()

        );

        return new LoginResponseDto(token);
    }

    private Boolean validateEmailIsUnique(String email){
       return personRepository.existsByEmail(email);
    }
}
