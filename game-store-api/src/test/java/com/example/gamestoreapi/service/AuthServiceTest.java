package com.example.gamestoreapi.service;

import com.example.gamestoreapi.exception.UnauthorizedException;
import com.example.gamestoreapi.model.dto.AccountLoginRequestDto;
import com.example.gamestoreapi.model.dto.AuthResponseDto;
import com.example.gamestoreapi.model.entity.Account;
import com.example.gamestoreapi.model.enums.AccountRole;
import com.example.gamestoreapi.repository.AccountRepository;
import com.example.gamestoreapi.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_Success() {
        AccountLoginRequestDto request = new AccountLoginRequestDto("user", "pass");
        Account account = Account.builder().login("user").password("encoded").role(AccountRole.ROLE_USER).build();

        when(accountRepository.findByLogin("user")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("pass", "encoded")).thenReturn(true);
        when(jwtService.generateToken(account)).thenReturn("token123");

        AuthResponseDto response = authService.login(request);

        assertEquals("token123", response.getToken());
    }

    @Test
    void login_ThrowsException_WhenUserNotFound() {
        AccountLoginRequestDto request = new AccountLoginRequestDto("user", "pass");
        when(accountRepository.findByLogin("user")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.login(request));
    }

    @Test
    void login_ThrowsException_WhenPasswordInvalid() {
        AccountLoginRequestDto request = new AccountLoginRequestDto("user", "wrongpass");
        Account account = Account.builder().login("user").password("encoded").build();

        when(accountRepository.findByLogin("user")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("wrongpass", "encoded")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(request));
    }
}