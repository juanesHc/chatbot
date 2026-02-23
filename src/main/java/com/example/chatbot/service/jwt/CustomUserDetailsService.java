package com.example.chatbot.service.jwt;

import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.entity.model.SecurityUser;
import com.example.chatbot.exception.LoginException;
import com.example.chatbot.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        PersonEntity personEntity =
                personRepository.findByEmail(email).orElseThrow(()->new LoginException("Couldnt find the user"));
        if(personEntity.equals(null)){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new SecurityUser(personEntity);
    }
}
