package com.example.gamestoreapi.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final com.example.gamestoreapi.security.JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.extractAllClaims(token);
            String login = claims.getSubject();
            String role = claims.get("role", String.class);

            if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        login,
                        null,
                        Collections.singletonList(authority)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception ignored) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}