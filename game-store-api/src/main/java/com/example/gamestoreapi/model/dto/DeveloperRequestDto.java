package com.example.gamestoreapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
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

    @PastOrPresent(message = "Foundation year cannot be in the future")
    private Integer foundationYear;
}