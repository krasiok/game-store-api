package com.example.gamestoreapi.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class GameRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal price;

    private LocalDate releaseDate;

    @NotNull(message = "Developer ID is required")
    private Long developerId;

    @NotEmpty(message = "At least one category ID is required")
    private Set<Long> categoryIds;
}