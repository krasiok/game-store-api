package com.example.gamestoreapi.repository;

import com.example.gamestoreapi.model.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    boolean existsByNameIgnoreCase(String name);
}