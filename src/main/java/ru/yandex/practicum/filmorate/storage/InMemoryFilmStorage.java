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
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        films.put(film.getId(), film);
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
        // Получаем топ-фильмы, отсортированные по убыванию количества лайков
        List<Film> topFilms = filmsLikes.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size())) // Сортировка по убыванию
                .limit(count) // Ограничение по количеству
                .map(Map.Entry::getKey) // Получаем id фильмов
                .map(films::get) // Получаем сами фильмы
                .toList();
        return topFilms;
    }


    @Override
    public void checkExistFilm(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с id %d не найден.", filmId));
        }
    }

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}