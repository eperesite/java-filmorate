package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final RatingRepository ratingRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;

    @Override
    public Film addFilm(Film filmNew) {

        if (filmNew.getGenres() != null) {
            final List<Long> genreIds = filmNew.getGenres().stream().map(Genre::getId).toList();
            final Collection<Genre> genres = genreRepository.findByIds(genreIds);
            if (genres.size() != genreIds.size()) {
                throw new ValidationException("Жанры не найдены");
            }
        }
        ratingRepository.findById(filmNew.getMpa().getId())
                .orElseThrow(() -> new ValidationException("MPA с " + filmNew.getMpa().getId() + "не найден"));
        return filmRepository.addFilm(filmNew);
    }

    @Override
    public Film updateFilm(Film filmNew) {
        filmRepository.getFilm(filmNew.getId())
                .orElseThrow(() -> new NotFoundException("Фильм с " + filmNew.getId() + "не найден"));

        ratingRepository.findById(filmNew.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("MPA с " + filmNew.getMpa().getId() + "не найден"));

        if (filmNew.getGenres() != null) {

            final List<Long> genreIds = filmNew.getGenres().stream().map(Genre::getId).toList();
            final Collection<Genre> genres = genreRepository.findByIds(genreIds);
            if (genres.size() != genreIds.size()) {
                throw new ValidationException("Для фильма " + filmNew.getId() + " указаны несуществующие жанры");
            }
        }
        return filmRepository.updateFilm(filmNew);
    }

    @Override
    public Film getFilmById(long id) {
        final Film film = filmRepository.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с " + id + "не найден"));

        return film;
    }


    @Override
    public Collection<Film> getFilmsList() {
        return filmRepository.getFilms();
    }

    @Override
    public Collection<Film> findPopular(int count) {
        return filmRepository.findPopular(count);
    }


    @Override
    public void addLike(long filmId, long userId) {
        User user = userRepository.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + "не найден"));
        filmRepository.setLike(filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        User user = userRepository.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + "не найден"));
        filmRepository.deleteLike(filmId, userId);
    }
}