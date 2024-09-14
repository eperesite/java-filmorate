package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Collection<Genre> getGenreList() {
        return genreRepository.getGenres();
    }

    public Genre findById(long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }
}
