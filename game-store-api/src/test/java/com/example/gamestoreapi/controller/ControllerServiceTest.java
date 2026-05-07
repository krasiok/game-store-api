package com.example.gamestoreapi.controller;

import com.example.gamestoreapi.exception.GlobalExceptionHandler;
import com.example.gamestoreapi.model.dto.CategoryRequestDto;
import com.example.gamestoreapi.model.dto.CategoryResponseDto;
import com.example.gamestoreapi.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllCategories_Returns200() throws Exception {
        when(categoryService.findAll()).thenReturn(List.of(new CategoryResponseDto(1L, "RPG")));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("RPG"));
    }

    @Test
    void createCategory_Returns201() throws Exception {
        CategoryRequestDto request = new CategoryRequestDto("Action");
        when(categoryService.addCategory(any())).thenReturn(new CategoryResponseDto(1L, "Action"));

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Action"));
    }

    @Test
    void updateCategory_Returns200() throws Exception {
        CategoryRequestDto request = new CategoryRequestDto("Updated");
        when(categoryService.updateCategory(eq(1L), any())).thenReturn(new CategoryResponseDto(1L, "Updated"));

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCategory_Returns204() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }
}