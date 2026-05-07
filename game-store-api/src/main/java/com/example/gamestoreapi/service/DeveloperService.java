package com.example.gamestoreapi.service;

import com.example.gamestoreapi.exception.ResourceNotFoundException;
import com.example.gamestoreapi.model.dto.DeveloperRequestDto;
import com.example.gamestoreapi.model.dto.DeveloperResponseDto;
import com.example.gamestoreapi.model.entity.Developer;
import com.example.gamestoreapi.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    @Transactional(readOnly = true)
    public List<DeveloperResponseDto> getAllDevelopers() {
        return developerRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeveloperResponseDto addDeveloper(DeveloperRequestDto dto) {
        if (developerRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("Developer with this name already exists");
        }
        Developer developer = Developer.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .websiteUrl(dto.getWebsiteUrl())
                .country(dto.getCountry())
                .foundationYear(dto.getFoundationYear())
                .build();
        return mapToDto(developerRepository.save(developer));
    }

    @Transactional
    public DeveloperResponseDto updateDeveloper(Long id, DeveloperRequestDto dto) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id: " + id));

        developer.setName(dto.getName());
        developer.setDescription(dto.getDescription());
        developer.setWebsiteUrl(dto.getWebsiteUrl());
        developer.setCountry(dto.getCountry());
        developer.setFoundationYear(dto.getFoundationYear());

        return mapToDto(developerRepository.save(developer));
    }

    @Transactional
    public void deleteDeveloper(Long id) {
        if (!developerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Developer not found with id: " + id);
        }
        developerRepository.deleteById(id);
    }

    private DeveloperResponseDto mapToDto(Developer developer) {
        return DeveloperResponseDto.builder()
                .id(developer.getId())
                .name(developer.getName())
                .description(developer.getDescription())
                .websiteUrl(developer.getWebsiteUrl())
                .country(developer.getCountry())
                .foundationYear(developer.getFoundationYear())
                .build();
    }
}