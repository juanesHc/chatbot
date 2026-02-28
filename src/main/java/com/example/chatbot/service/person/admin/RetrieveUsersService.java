package com.example.chatbot.service.person.admin;


import com.example.chatbot.dto.user.request.RetrieveUsersFilterRequestDto;
import com.example.chatbot.dto.user.response.PagedUsersResponseDto;
import com.example.chatbot.entity.PersonEntity;
import com.example.chatbot.mapper.person.PersonMapper;
import com.example.chatbot.repository.PersonRepository;
import com.example.chatbot.specification.UsersSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetrieveUsersService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PagedUsersResponseDto retrieveUsers(RetrieveUsersFilterRequestDto filter, Pageable pageable) {
        Specification<PersonEntity> spec = UsersSpecification.buildUserSpecification(filter);
        Page<PersonEntity> page = personRepository.findAll(spec, pageable);

        return PagedUsersResponseDto.builder()
                .content(page.getContent().stream()
                        .map(personMapper::PersonEntityToRetrieveUsersFilterDto)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
