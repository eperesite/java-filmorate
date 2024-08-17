package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.InvalidReleaseDateException;

import java.time.LocalDate;

@RestController
@RequestMapping("/films")
public class FilmController {
    // Ваши зависимости и другие методы

    @PostMapping
    public Film create(@RequestBody Film film) {
        validateReleaseDate(film.getReleaseDate());
        // Логика сохранения фильма
        film.setId(generateId()); // Пример генерации ID
        return film;
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) { // К примеру, дата начала киноиндустрии
            throw new InvalidReleaseDateException("Дата выпуска фильма не может быть раньше 28 декабря 1895 года.");
        }
    }

    private int generateId() {
        // Пример генерации ID, возможно у вас другой способ
        return (int) (Math.random() * 1000);
    }
}