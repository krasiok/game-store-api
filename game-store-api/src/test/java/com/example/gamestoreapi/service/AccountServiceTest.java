package com.example.gamestoreapi.service;

import com.example.gamestoreapi.model.dto.AccountRegisterRequestDto;
import com.example.gamestoreapi.model.entity.Account;
import com.example.gamestoreapi.model.enums.AccountRole;
import com.example.gamestoreapi.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_Success() {
        AccountRegisterRequestDto dto = new AccountRegisterRequestDto("user", "pass");
        when(accountRepository.existsByLogin("user")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");

        accountService.createAccount(dto);

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_ThrowsException_WhenLoginExists() {
        AccountRegisterRequestDto dto = new AccountRegisterRequestDto("user", "pass");
        when(accountRepository.existsByLogin("user")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void createAdmin_Success() {
        when(passwordEncoder.encode("adminpass")).thenReturn("encoded");

        accountService.createAdmin("admin", "adminpass");

        verify(accountRepository).save(argThat(account ->
                account.getLogin().equals("admin") && account.getRole() == AccountRole.ROLE_ADMIN));
    }

    @Test
    void existsByLogin_ReturnsTrue() {
        when(accountRepository.existsByLogin("test")).thenReturn(true);
        assertTrue(accountService.existsByLogin("test"));
    }
}