package com.example.gamestoreapi.controller;


import com.example.gamestoreapi.model.dto.AccountRegisterRequestDto;
import com.example.gamestoreapi.model.entity.Account;
import com.example.gamestoreapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminPanelController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountRegisterRequestDto> addAccount(@RequestBody AccountRegisterRequestDto accountRegisterDto) {
        accountService.createAdmin(accountRegisterDto.getLogin(), accountRegisterDto.getPassword());
        return ResponseEntity.ok(accountRegisterDto);
    }
}
