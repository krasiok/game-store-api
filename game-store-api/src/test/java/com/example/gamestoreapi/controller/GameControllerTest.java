package com.example.gamestoreapi.controller;

import com.example.gamestoreapi.exception.GlobalExceptionHandler;
import com.example.gamestoreapi.model.dto.GameRequestDto;
import com.example.gamestoreapi.model.dto.GameResponseDto;
import com.example.gamestoreapi.service.GameService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchGames_Returns200() throws Exception {
        GameResponseDto response = GameResponseDto.builder().id(1L).title("Test").build();
        when(gameService.searchGames(any(), any(), any(), any())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/games?title=Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test"));
    }

    @Test
    void createGame_Returns201() throws Exception {
        GameRequestDto request = GameRequestDto.builder()
                .title("New Game")
                .price(BigDecimal.TEN)
                .developerId(1L)
                .categoryIds(Set.of(1L))
                .build();

        GameResponseDto response = GameResponseDto.builder().id(1L).title("New Game").build();
        when(gameService.addGame(any())).thenReturn(response);

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Game"));
    }
}