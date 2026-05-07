package com.example.gamestoreapi.controller;

import com.example.gamestoreapi.model.dto.DeveloperRequestDto;
import com.example.gamestoreapi.model.dto.DeveloperResponseDto;
import com.example.gamestoreapi.service.DeveloperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;

    @GetMapping
    public ResponseEntity<List<DeveloperResponseDto>> getAllDevelopers() {
        return ResponseEntity.ok(developerService.getAllDevelopers());
    }

    @PostMapping
    public ResponseEntity<DeveloperResponseDto> addDeveloper(@Valid @RequestBody DeveloperRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(developerService.addDeveloper(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeveloperResponseDto> updateDeveloper(@PathVariable Long id, @Valid @RequestBody DeveloperRequestDto dto) {
        return ResponseEntity.ok(developerService.updateDeveloper(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        developerService.deleteDeveloper(id);
        return ResponseEntity.noContent().build();
    }
}