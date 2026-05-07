package com.example.gamestoreapi.service;

import com.example.gamestoreapi.exception.ResourceNotFoundException;
import com.example.gamestoreapi.model.dto.CategoryResponseDto;
import com.example.gamestoreapi.model.dto.DeveloperResponseDto;
import com.example.gamestoreapi.model.dto.GameRequestDto;
import com.example.gamestoreapi.model.dto.GameResponseDto;
import com.example.gamestoreapi.model.entity.Category;
import com.example.gamestoreapi.model.entity.Developer;
import com.example.gamestoreapi.model.entity.Game;
import com.example.gamestoreapi.repository.CategoryRepository;
import com.example.gamestoreapi.repository.DeveloperRepository;
import com.example.gamestoreapi.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<GameResponseDto> searchGames(String title, BigDecimal minPrice, BigDecimal maxPrice, Long developerId) {
        return gameRepository.searchGames(title, minPrice, maxPrice, developerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public GameResponseDto addGame(GameRequestDto dto) {
        Developer developer = developerRepository.findById(dto.getDeveloperId())
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id: " + dto.getDeveloperId()));

        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));
        if (categories.isEmpty()) {
            throw new IllegalArgumentException("Provided categories are invalid or do not exist");
        }

        Game game = Game.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .releaseDate(dto.getReleaseDate())
                .developer(developer)
                .categories(categories)
                .build();

        return mapToDto(gameRepository.save(game));
    }

    @Transactional
    public GameResponseDto updateGame(Long id, GameRequestDto dto) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + id));

        Developer developer = developerRepository.findById(dto.getDeveloperId())
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id: " + dto.getDeveloperId()));

        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));

        game.setTitle(dto.getTitle());
        game.setDescription(dto.getDescription());
        game.setPrice(dto.getPrice());
        game.setReleaseDate(dto.getReleaseDate());
        game.setDeveloper(developer);
        game.setCategories(categories);

        return mapToDto(gameRepository.save(game));
    }

    @Transactional
    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Game not found with id: " + id);
        }
        gameRepository.deleteById(id);
    }

    private GameResponseDto mapToDto(Game game) {
        DeveloperResponseDto developerDto = DeveloperResponseDto.builder()
                .id(game.getDeveloper().getId())
                .name(game.getDeveloper().getName())
                .description(game.getDeveloper().getDescription())
                .websiteUrl(game.getDeveloper().getWebsiteUrl())
                .country(game.getDeveloper().getCountry())
                .foundationYear(game.getDeveloper().getFoundationYear())
                .build();

        Set<CategoryResponseDto> categoryDtos = game.getCategories().stream()
                .map(cat -> CategoryResponseDto.builder()
                        .id(cat.getId())
                        .name(cat.getName())
                        .build())
                .collect(Collectors.toSet());

        return GameResponseDto.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .price(game.getPrice())
                .releaseDate(game.getReleaseDate())
                .developer(developerDto)
                .categories(categoryDtos)
                .build();
    }
}