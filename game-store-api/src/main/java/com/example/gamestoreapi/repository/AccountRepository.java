package com.example.gamestoreapi.repository;

import com.example.gamestoreapi.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByLogin(String login);
    boolean existsByLogin(String login);
}