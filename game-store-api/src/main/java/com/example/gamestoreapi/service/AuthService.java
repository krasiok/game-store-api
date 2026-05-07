package com.example.gamestoreapi.service;

import com.example.gamestoreapi.exception.UnauthorizedException;
import com.example.gamestoreapi.model.dto.AccountLoginRequestDto;
import com.example.gamestoreapi.model.dto.AuthResponseDto;
import com.example.gamestoreapi.model.entity.Account;
import com.example.gamestoreapi.repository.AccountRepository;
import com.example.gamestoreapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public AuthResponseDto login(AccountLoginRequestDto loginRequest) {
        Account account = accountRepository.findByLogin(loginRequest.getLogin())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = jwtService.generateToken(account);
        return new AuthResponseDto(token);
    }
}