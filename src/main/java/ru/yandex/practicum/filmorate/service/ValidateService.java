package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class ValidateService {
    private final LocalDate birthdayOfCinema = LocalDate.of(1895, 12, 28);

    public void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Имя должно быть указано");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email должен быть указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин должен быть указан");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин содержит недопустимые символы (пробелы)");
        }
    }

    public void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(birthdayOfCinema)) {
            throw new ValidationException("Некорректная дата релиза, не может быть раньше 28.12.1895");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
