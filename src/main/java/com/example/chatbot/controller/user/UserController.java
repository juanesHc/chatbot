package com.example.chatbot.controller.user;

import com.example.chatbot.dto.user.request.UpdateUserAccountRequestDto;
import com.example.chatbot.dto.user.response.DeleteUserResponseDto;
import com.example.chatbot.dto.user.response.RetrievePersonDataResponseDto;
import com.example.chatbot.dto.user.response.UpdateUserAccountResponseDto;
import com.example.chatbot.service.person.user.MyAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class UserController {

    private final MyAccountService myAccountService;

    @GetMapping("/see/{personId}")
    public ResponseEntity<RetrievePersonDataResponseDto> getPersonData(@PathVariable String personId){
        return ResponseEntity.ok(myAccountService.retrieveMyData(personId));
    }

    @DeleteMapping("/delete/{personId}")
    public ResponseEntity<DeleteUserResponseDto> deleteAccount(@PathVariable String personId){
        return ResponseEntity.ok(myAccountService.dropMyAccount(personId));
    }

    @PutMapping("/edit/{personId}")
    public ResponseEntity<UpdateUserAccountResponseDto> putAccount(@PathVariable String personId,
                                                                   @RequestBody  UpdateUserAccountRequestDto updateUserAccountRequestDto){
        return ResponseEntity.ok(myAccountService.editMyData(personId,updateUserAccountRequestDto));
    }

}
