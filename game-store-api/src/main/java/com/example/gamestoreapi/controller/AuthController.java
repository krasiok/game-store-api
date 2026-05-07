package com.example.gamestoreapi.controller;

import com.example.gamestoreapi.model.dto.AccountLoginRequestDto;
import com.example.gamestoreapi.model.dto.AuthResponseDto;
import com.example.gamestoreapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AccountLoginRequestDto loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}