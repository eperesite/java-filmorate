package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        log.info("Добавление нового фильма: {}", film);
        Film createdFilm = filmService.addFilm(film);
        log.info("Фильм успешно добавлен: {}", createdFilm);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Фильм успешно обновлен: {}", updatedFilm);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.info("Получение всех фильмов");
        List<Film> filmsList = filmService.getFilms();
        log.info("Получено {} фильмов", filmsList.size());
        return ResponseEntity.ok(filmsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable int id) {
        log.info("Получение фильма с ID: {}", id);
        Film film = filmService.getFilm(id);
        log.info("Получен фильм: {}", film);
        return ResponseEntity.ok().body(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Добавление лайка от пользователя {} к фильму {}", userId, filmId);
        filmService.addLike(filmId, userId);
        log.info("Лайк успешно добавлен");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Удаление лайка от пользователя {} к фильму {}", userId, filmId);
        filmService.removeLike(filmId, userId);
        log.info("Лайк успешно удален");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение топ {} фильмов", count);
        List<Film> topFilms = filmService.getTopFilms(count);
        log.info("Получено топ {} фильмов", topFilms.size());
        return ResponseEntity.ok(topFilms);
    }
}
