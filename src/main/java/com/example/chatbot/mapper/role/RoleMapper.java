package com.example.chatbot.mapper.role;

import com.example.chatbot.dto.role.RetrieveRolesResponseDto;
import com.example.chatbot.entity.enums.RoleEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleMapper {

    public RetrieveRolesResponseDto roleEnumToRetrieveRoleResponse(List<RoleEnum> roleEnumList){
        List<String> list=new ArrayList<>();

        roleEnumList.forEach(role->list.add(String.valueOf(role)));
        return new RetrieveRolesResponseDto(list);
    }

}
