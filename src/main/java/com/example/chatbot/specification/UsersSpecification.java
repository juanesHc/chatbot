package com.example.chatbot.specification;

import com.example.chatbot.dto.user.request.RetrieveUsersFilterRequestDto;
import com.example.chatbot.entity.PersonEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UsersSpecification {

    private static Specification<PersonEntity> hasGivenName(String givenName){
        return ((root, query, cb) -> cb.like(
                cb.lower(root.get("givenName")),"%"+givenName.toLowerCase()+"%"
        ));
    }

    private static Specification<PersonEntity> hasFamilyName(String familyName){
        return ((root, query, cb) -> cb.like(
                cb.lower(root.get("familyName")),"%"+familyName.toLowerCase()+"%"
        ));
    }

    private static Specification<PersonEntity> hasRole(String roleName) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("role").get("type"),
                        roleName
                );
    }

    private static Specification<PersonEntity> hasCreatedAt(LocalDate createdAt) {
        return (root, query, cb) ->
                cb.between(
                        root.get("createdAt"),
                        createdAt.atStartOfDay(),
                        createdAt.atTime(23, 59, 59)
                );
    }


    private static Specification<PersonEntity> hasEmail(String email) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("email")), email.toLowerCase());
    }

    private static Specification<PersonEntity> hasProvider(String provider) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("provider")), provider.toLowerCase());
    }

    private static Specification<PersonEntity> createdBetween(
            LocalDate sourceDate,
            LocalDate targetDate
    ) {
        return (root, query, cb) ->
                cb.between(
                        root.get("createdAt"),
                        sourceDate.atStartOfDay(),
                        targetDate.atTime(23, 59, 59)
                );
    }

    public static Specification<PersonEntity> buildUserSpecification(RetrieveUsersFilterRequestDto retrieveUsersFilterRequestDto){

        Specification<PersonEntity> personEntitySpecification = Specification.allOf();

        if(retrieveUsersFilterRequestDto.getGivenName()!=null && !retrieveUsersFilterRequestDto.getGivenName().isBlank()){
            personEntitySpecification=personEntitySpecification.and(hasGivenName(retrieveUsersFilterRequestDto.getGivenName()));
        }
        if(retrieveUsersFilterRequestDto.getFamilyName()!=null && !retrieveUsersFilterRequestDto.getFamilyName().isBlank()){
            personEntitySpecification=personEntitySpecification.and(hasFamilyName(retrieveUsersFilterRequestDto.getFamilyName()));
        }
        if(retrieveUsersFilterRequestDto.getEmail()!=null && !retrieveUsersFilterRequestDto.getEmail().isBlank()){
            personEntitySpecification=personEntitySpecification.and(hasEmail(retrieveUsersFilterRequestDto.getEmail()));
        }

        if(retrieveUsersFilterRequestDto.getRoleName()!=null && !retrieveUsersFilterRequestDto.getRoleName().isBlank()){
            personEntitySpecification=personEntitySpecification.and(hasRole(retrieveUsersFilterRequestDto.getRoleName()));
        }

        if (retrieveUsersFilterRequestDto.getCreatedAt() != null) {
            personEntitySpecification = personEntitySpecification.and(hasCreatedAt(retrieveUsersFilterRequestDto.getCreatedAt()));
        }

        if (retrieveUsersFilterRequestDto.getSourceDate() != null && retrieveUsersFilterRequestDto.getTargetDate() != null) {
            personEntitySpecification = personEntitySpecification.and(
                    createdBetween(
                            retrieveUsersFilterRequestDto.getSourceDate(),
                            retrieveUsersFilterRequestDto.getTargetDate()
                    )
            );
        }


        return personEntitySpecification;
    }



}
