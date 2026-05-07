package com.example.gamestoreapi.service;

import com.example.gamestoreapi.exception.ResourceNotFoundException;
import com.example.gamestoreapi.model.dto.DeveloperRequestDto;
import com.example.gamestoreapi.model.entity.Developer;
import com.example.gamestoreapi.repository.DeveloperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperService developerService;

    @Test
    void addDeveloper_ThrowsException_WhenExists() {
        DeveloperRequestDto request = new DeveloperRequestDto("Dev", "Desc", "url", "PL", 2000);
        when(developerRepository.existsByNameIgnoreCase("Dev")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> developerService.addDeveloper(request));
    }

    @Test
    void updateDeveloper_ThrowsException_WhenNotFound() {
        when(developerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> developerService.updateDeveloper(1L, new DeveloperRequestDto()));
    }

    @Test
    void deleteDeveloper_ThrowsException_WhenNotFound() {
        when(developerRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> developerService.deleteDeveloper(1L));
    }
}