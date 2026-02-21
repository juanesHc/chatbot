package com.example.chatbot.config.security;

import com.example.chatbot.dto.login.response.LoginResponseDto;
import com.example.chatbot.dto.user.request.RegisterUserRequestDto;
import com.example.chatbot.service.person.user.RegisterUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2SuccessfulHandler implements AuthenticationSuccessHandler {

    private final RegisterUserService registerUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String givenName = oAuth2User.getAttribute("given_name");
        String familyName = oAuth2User.getAttribute("family_name");

        String registrationId =
                ((OAuth2AuthenticationToken) authentication)
                        .getAuthorizedClientRegistrationId();

        if (!"google".equals(registrationId)) {
            throw new IllegalStateException("Oauth2 provider unsupported");
        }

        LoginResponseDto authResponse =
                registerUserService.registerByGoogleProvider(
                        new RegisterUserRequestDto(email, givenName,familyName)
                );

        String redirectUrl = "http://localhost:4200/login?token=" + authResponse.getToken();
        response.sendRedirect(redirectUrl);

    }

}
