package com.example.chatbot.service.person.user;

import com.example.chatbot.dto.login.response.LoginResponseDto;
import com.example.chatbot.dto.notification.request.RegisterNotificationRequestDto;
import com.example.chatbot.dto.user.request.RegisterUserRequestDto;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.RoleEntity;
import com.example.chatbot.entity.enums.RoleEnum;
import com.example.chatbot.entity.model.SecurityUser;
import com.example.chatbot.exception.RegisterUserException;
import com.example.chatbot.mapper.person.PersonMapper;
import com.example.chatbot.repository.PersonRepository;
import com.example.chatbot.repository.RoleRepository;
import com.example.chatbot.service.jwt.JwtService;
import com.example.chatbot.service.messaging.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService {

    private final JwtService jwtService;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final RoleRepository roleRepository;
    private final MessagingService messagingService;

    public LoginResponseDto registerByGoogleProvider(RegisterUserRequestDto OAuth2GoogleRequestDto) {

        return personRepository.findByEmail(OAuth2GoogleRequestDto.getEmail())
                .map(this::generateTokenGoogle)
                .orElseGet(() -> createGoogleUser(OAuth2GoogleRequestDto));
    }



    private LoginResponseDto createGoogleUser(RegisterUserRequestDto OAuth2GoogleRequestDto) {

        PersonEntity newUser = personMapper.registerGoogleRequestDtoToPersonEntity(OAuth2GoogleRequestDto);

        RoleEntity roleEntity=roleRepository.findByType(RoleEnum.USER).orElseThrow(()->new RegisterUserException("Couldnt add role"));
        newUser.setRoleEntity(roleEntity);
        PersonEntity personSaved=personRepository.save(newUser);

        RegisterNotificationRequestDto registerNotificationRequestDto=new RegisterNotificationRequestDto();
        registerNotificationRequestDto.setSubject("Welcome");
        registerNotificationRequestDto.setMessageDescription("Hello "+personSaved.getFirstName()+" Welcome to ChatBotApp");

        messagingService.registerInternalNotification(String.valueOf(personSaved.getId()),registerNotificationRequestDto);

        return generateTokenGoogle(newUser);

    }


    private LoginResponseDto generateTokenGoogle(PersonEntity personEntity) {

        UserDetails userDetails = new SecurityUser(personEntity);

        String token = jwtService.generateToken(
                userDetails,
                personEntity.getId(),
                personEntity.getFirstName()

        );

        return new LoginResponseDto(token);
    }

}
