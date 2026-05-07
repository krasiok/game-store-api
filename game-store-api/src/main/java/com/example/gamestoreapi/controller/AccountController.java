package com.example.gamestoreapi.controller;

import com.example.gamestoreapi.model.dto.AccountRegisterRequestDto;
import com.example.gamestoreapi.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AccountRegisterRequestDto dto) {
        accountService.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}