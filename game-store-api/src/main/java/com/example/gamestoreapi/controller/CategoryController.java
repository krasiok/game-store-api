package com.example.gamestoreapi.controller;

import com.example.gamestoreapi.model.dto.CategoryRequestDto;
import com.example.gamestoreapi.model.dto.CategoryResponseDto;
import com.example.gamestoreapi.service.CategoryService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto dto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}