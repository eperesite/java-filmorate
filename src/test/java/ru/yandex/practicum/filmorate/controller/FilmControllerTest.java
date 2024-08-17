package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.InvalidReleaseDateException;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private final FilmController filmController = new FilmController();

    @Test
    void shouldNotValidateReleaseDate() {
        Film invalidFilm = new Film(0, "The Green Mile", "Sad film", LocalDate.of(1000, Month.DECEMBER, 6), 189);

        assertThrows(InvalidReleaseDateException.class, () -> {
            filmController.create(invalidFilm);
        });
    }
}
