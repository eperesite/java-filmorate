package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private final HashMap<Long, Set<Long>> filmsLikes = new HashMap<>();


    @Override
    public List<Film> getAll() {
        return films.values().stream().toList();
    }

    @Override
    public Optional<Film> get(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film save(Film film) {
        film.setId(getNextId());
        films.put((long) film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        films.put((long) film.getId(), film);
        return film;
    }

    @Override
    public void addLike(long filmId, long userId) {
        Set<Long> usersLikes = filmsLikes.computeIfAbsent(filmId, k -> new HashSet<>());
        usersLikes.add(userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        if (!filmsLikes.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с id %d не найден", filmId));
        }
        if (!filmsLikes.get(filmId).contains(userId)) {
            throw new NotFoundException("Нет лайка от пользователя с id = " + userId);
        }
        filmsLikes.get(filmId).remove(userId);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        count = Math.min(count, films.size());

        List<Film> topFilms = filmsLikes.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size())) // сортируем по убыванию
                .map(Map.Entry::getKey)
                .map(films::get)
                .toList();

        return topFilms.subList(0, count);
    }

    @Override
    public void checkExistFilm(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с id %d не найден.", filmId));
        }
    }

    private int getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return (int) ++currentMaxId;
    }
}