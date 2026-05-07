package com.example.gamestoreapi.controller;

import com.example.gamestoreapi.model.dto.GameRequestDto;
import com.example.gamestoreapi.model.dto.GameResponseDto;
import com.example.gamestoreapi.service.GameService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<List<GameResponseDto>> searchGames(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long developerId) {
        return ResponseEntity.ok(gameService.searchGames(title, minPrice, maxPrice, developerId));
    }

    @PostMapping
    public ResponseEntity<GameResponseDto> createGame(@Valid @RequestBody GameRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.addGame(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDto> updateGame(@PathVariable Long id, @Valid @RequestBody GameRequestDto dto) {
        return ResponseEntity.ok(gameService.updateGame(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}