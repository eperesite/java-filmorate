package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final ValidateService validateService;


    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        validateService.validateFilm(film);
        return new ResponseEntity<>(filmService.addFilm(film), HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film filmNewInfo) {
        validateService.validateFilm(filmNewInfo);
        return new ResponseEntity<>(filmService.updateFilm(filmNewInfo), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        return new ResponseEntity<>(filmService.getAllFilms(), HttpStatus.OK);
    }

    @GetMapping("popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("{id}")
    public Film getFilm(@PathVariable long id) {
        return filmService.getFilm(id);
    }

    @PutMapping("{id}/like/{user-id}")
    public void addLike(@PathVariable long id, @PathVariable("user-id") long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{user-id}")
    public void removeLike(@PathVariable long id, @PathVariable("user-id") long userId) {
        filmService.removeLike(id, userId);
    }
}

