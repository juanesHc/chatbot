package com.example.chatbot.service.person.user;

import com.example.chatbot.dto.user.request.UpdateUserAccountRequestDto;
import com.example.chatbot.dto.user.response.DeleteUserResponseDto;
import com.example.chatbot.dto.user.response.RetrievePersonDataResponseDto;
import com.example.chatbot.dto.user.response.UpdateUserAccountResponseDto;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.exception.RetrievePersonException;
import com.example.chatbot.exception.UpdateUserException;
import com.example.chatbot.mapper.person.PersonMapper;
import com.example.chatbot.repository.ChatRepository;
import com.example.chatbot.repository.MessageRepository;
import com.example.chatbot.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyAccountService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public RetrievePersonDataResponseDto retrieveMyData(String personId){
        PersonEntity personEntity=personRepository.findById(UUID.fromString(personId)).
                orElseThrow(()->new RetrievePersonException("Couldnt found the person"));

        return personMapper.personEntityToRetrievePersonDataResponseDto(personEntity);
    }

    @Transactional
    public UpdateUserAccountResponseDto editMyData(String personId , UpdateUserAccountRequestDto updateUserAccountRequestDto){
        PersonEntity personEntity=personRepository.findById(UUID.fromString(personId)).
                orElseThrow(()->new RetrievePersonException("Couldnt found the person"));
        if(updateUserAccountRequestDto.getFirstName() != null){
            if(!validateNewName(updateUserAccountRequestDto.getFirstName())){
                throw new UpdateUserException("User firstname is not valid, must be between 3 and 20");
            }
            personEntity.setFirstName(updateUserAccountRequestDto.getFirstName());
            personEntity.setUpdatedAt(LocalDateTime.now());
        }

        if(updateUserAccountRequestDto.getLastName() != null){
            if(!validateNewName(updateUserAccountRequestDto.getLastName())){
                throw new UpdateUserException("User lastname is not valid, must be between 3 and 20");
            }
            personEntity.setLastName(updateUserAccountRequestDto.getLastName());
            personEntity.setUpdatedAt(LocalDateTime.now());
        }
        personRepository.save(personEntity);
        log.warn("Data successfully updated");
        return new UpdateUserAccountResponseDto("Camps successfully update");
    }

    @Transactional
    public DeleteUserResponseDto dropMyAccount(String personId){
        PersonEntity personEntity=personRepository.findById(UUID.fromString(personId)).
                orElseThrow(()->new RetrievePersonException("Couldnt found the person"));
        personRepository.delete(personEntity);
        return new DeleteUserResponseDto("Account successfully dropped");
    }

    private Boolean validateNewName(String name){
        return name.length() >= 3 && name.length() <= 20;
    }

}
