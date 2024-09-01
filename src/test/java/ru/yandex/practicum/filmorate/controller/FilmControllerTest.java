package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAll() throws Exception {
        Film film = new Film(1L, "Film Title", "Film Description", LocalDate.of(2022, 1, 1), 120);
        when(filmService.getAll()).thenReturn(Collections.singletonList(film));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(film.getId()))
                .andExpect(jsonPath("$[0].name").value(film.getName()))
                .andExpect(jsonPath("$[0].description").value(film.getDescription()))
                .andExpect(jsonPath("$[0].releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$[0].duration").value(film.getDuration()));
    }

    @Test
    void testCreate() throws Exception {
        Film film = new Film(1L, "Film Title", "Film Description", LocalDate.of(2022, 1, 1), 120);
        when(filmService.save(any(Film.class))).thenReturn(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()));
    }

    @Test
    void testUpdate() throws Exception {
        Film film = new Film(1L, "Updated Film Title", "Updated Film Description", LocalDate.of(2022, 1, 1), 130);
        when(filmService.update(any(Film.class))).thenReturn(film);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()));
    }

    @Test
    void testLike() throws Exception {
        doNothing().when(filmService).addLike(1L, 2L);

        mockMvc.perform(put("/films/1/like/2"))
                .andExpect(status().isOk());

        verify(filmService, times(1)).addLike(1L, 2L);
    }

    @Test
    void testDeleteLike() throws Exception {
        doNothing().when(filmService).deleteLike(1L, 2L);

        mockMvc.perform(delete("/films/1/like/2"))
                .andExpect(status().isOk());

        verify(filmService, times(1)).deleteLike(1L, 2L);
    }

    @Test
    void testGetPopular() throws Exception {
        Film film = new Film(1L, "Popular Film", "Popular Film Description", LocalDate.of(2022, 1, 1), 120);
        when(filmService.getPopular(10)).thenReturn(Collections.singletonList(film));

        mockMvc.perform(get("/films/popular")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(film.getId()))
                .andExpect(jsonPath("$[0].name").value(film.getName()))
                .andExpect(jsonPath("$[0].description").value(film.getDescription()))
                .andExpect(jsonPath("$[0].releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$[0].duration").value(film.getDuration()));
    }
}
