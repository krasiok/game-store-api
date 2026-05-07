package com.example.gamestoreapi.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperRequestDto {

    @NotBlank(message = "Developer name cannot be empty")
    private String name;

    private String description;
    private String websiteUrl;
    private String country;

    @Min(value = 1950, message = "Foundation year cannot be older than 1950")
    @Max(value = 2026, message = "Foundation year cannot be in the future")
    private Integer foundationYear;
}