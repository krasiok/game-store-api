package com.example.gamestoreapi.security;

import com.example.gamestoreapi.model.entity.Account;
import com.example.gamestoreapi.model.enums.AccountRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "MojaBardzoDlugaITajnaSekretnaWartoscDoPodpisywaniaJWT123!");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);
    }

    @Test
    void generateToken_AndExtractClaims_Success() {
        Account account = Account.builder().login("admin").role(AccountRole.ROLE_ADMIN).build();

        String token = jwtService.generateToken(account);
        assertNotNull(token);

        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("admin", claims.getSubject());
        assertEquals("ROLE_ADMIN", claims.get("role"));
    }
}