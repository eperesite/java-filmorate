package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BaseGenreService implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Collection<Genre> getGenreList() {
        return genreRepository.getGenres();
    }

    @Override
    public Genre findById(long id) {
        final Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с " + id + "не найден"));
        return genre;
    }
}
