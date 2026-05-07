package com.example.gamestoreapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponseDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDate releaseDate;
    private DeveloperResponseDto developer;
    private Set<CategoryResponseDto> categories;
}