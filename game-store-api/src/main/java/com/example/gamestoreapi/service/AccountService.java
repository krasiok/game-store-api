package com.example.gamestoreapi.service;

import com.example.gamestoreapi.model.dto.AccountRegisterRequestDto;
import com.example.gamestoreapi.model.entity.Account;
import com.example.gamestoreapi.model.enums.AccountRole;
import com.example.gamestoreapi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createAccount(AccountRegisterRequestDto dto) {
        if (accountRepository.existsByLogin(dto.getLogin())) {
            throw new IllegalArgumentException("Login already exists");
        }

        Account account = Account.builder()
                .login(dto.getLogin())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(AccountRole.ROLE_USER)
                .build();
        accountRepository.save(account);
    }

    @Transactional
    public void createAdmin(String login, String password) {
        Account account = Account.builder()
                .login(login)
                .password(passwordEncoder.encode(password))
                .role(AccountRole.ROLE_ADMIN)
                .build();
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public boolean existsByLogin(String login) {
        return accountRepository.existsByLogin(login);
    }
}