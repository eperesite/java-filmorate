package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    private Film film;

    @Test
    void shouldCreateFilm() throws Exception {
        when(filmService.save(any(Film.class))).thenReturn(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()));
    }

    @Test
    void shouldUpdateFilm() throws Exception {
        when(filmService.update(any(Film.class))).thenReturn(film);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()));
    }

    @Test
    void shouldReturnAllFilms() throws Exception {
        when(filmService.getAll()).thenReturn(List.of(film));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(film.getId()))
                .andExpect(jsonPath("$[0].name").value(film.getName()));
    }

    @Test
    void shouldAddLike() throws Exception {
        mockMvc.perform(put("/films/1/like/2"))
                .andExpect(status().isOk());
        verify(filmService, times(1)).addLike(1L, 2L);
    }

    @Test
    void shouldDeleteLike() throws Exception {
        mockMvc.perform(delete("/films/1/like/2"))
                .andExpect(status().isOk());
        verify(filmService, times(1)).deleteLike(1L, 2L);
    }

    @Test
    void shouldReturnPopularFilms() throws Exception {
        when(filmService.getPopular(10)).thenReturn(Collections.singletonList(film));

        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(film.getId()))
                .andExpect(jsonPath("$[0].name").value(film.getName()));
    }
}
