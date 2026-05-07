package com.example.gamestoreapi.config;

import com.example.gamestoreapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final AccountService accountService;

    @Override
    public void run(String... args) {
        String login = "admin";
        String password = "admin";

        if (!accountService.existsByLogin(login)) {
            accountService.createAdmin(login, password);
        }
    }
}