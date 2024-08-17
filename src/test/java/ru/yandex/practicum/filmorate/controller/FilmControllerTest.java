package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = new Film(0, "The Green Mile", "Sad film", LocalDate.of(1999, Month.DECEMBER, 6), 189);
    }

    @Test
    void shouldNotValidateReleaseDate() {
        Film newFilm = new Film(0, "The Green Mile", "Sad film", LocalDate.of(1000, Month.DECEMBER, 6), 189);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> filmController.create(newFilm));
        assertEquals("Дата выпуска должна быть после 28 декабря 1895 года", exception.getMessage());
    }
}
