package com.example.gamestoreapi.service;

import com.example.gamestoreapi.exception.ResourceNotFoundException;
import com.example.gamestoreapi.model.dto.CategoryRequestDto;
import com.example.gamestoreapi.model.dto.CategoryResponseDto;
import com.example.gamestoreapi.model.entity.Category;
import com.example.gamestoreapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDto addCategory(CategoryRequestDto dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }
        Category category = Category.builder().name(dto.getName()).build();
        return mapToDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setName(dto.getName());
        return mapToDto(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private CategoryResponseDto mapToDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}