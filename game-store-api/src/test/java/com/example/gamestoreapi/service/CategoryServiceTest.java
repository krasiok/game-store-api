package com.example.gamestoreapi.service;

import com.example.gamestoreapi.exception.ResourceNotFoundException;
import com.example.gamestoreapi.model.dto.CategoryRequestDto;
import com.example.gamestoreapi.model.dto.CategoryResponseDto;
import com.example.gamestoreapi.model.entity.Category;
import com.example.gamestoreapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findAll_ReturnsList() {
        when(categoryRepository.findAll()).thenReturn(List.of(Category.builder().id(1L).name("RPG").build()));
        List<CategoryResponseDto> result = categoryService.findAll();
        assertEquals(1, result.size());
        assertEquals("RPG", result.get(0).getName());
    }

    @Test
    void addCategory_Success() {
        CategoryRequestDto request = new CategoryRequestDto("Action");
        when(categoryRepository.existsByNameIgnoreCase("Action")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(Category.builder().id(1L).name("Action").build());

        CategoryResponseDto response = categoryService.addCategory(request);
        assertEquals("Action", response.getName());
    }

    @Test
    void updateCategory_Success() {
        CategoryRequestDto request = new CategoryRequestDto("NewName");
        Category existing = Category.builder().id(1L).name("OldName").build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(existing);

        CategoryResponseDto response = categoryService.updateCategory(1L, request);
        assertEquals("NewName", response.getName());
    }

    @Test
    void updateCategory_ThrowsException_WhenNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(1L, new CategoryRequestDto("A")));
    }

    @Test
    void deleteCategory_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        categoryService.deleteCategory(1L);
        verify(categoryRepository).deleteById(1L);
    }
}