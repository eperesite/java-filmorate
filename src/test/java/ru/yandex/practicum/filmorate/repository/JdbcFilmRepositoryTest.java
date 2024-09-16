package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import(JdbcFilmRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFilmRepository")
public class JdbcFilmRepositoryTest {
    private final FilmRepository filmRepository;

    static Rating testRating() {
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setName("G");
        return rating;
    }

    static Genre testGenre() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Криминал");
        return genre;
    }

    static Film compareTestFilm() {
        LinkedHashSet<Genre> setGenres = new LinkedHashSet<>();
        setGenres.add(testGenre());
        Film film = new Film();
        film.setId(1L);
        film.setDescription("Гангстеры делят наркоферму");
        film.setName("Джентельмены");
        film.setMpa(testRating());
        film.setGenres(setGenres);
        film.setDuration(113);
        film.setReleaseDate(LocalDate.of(2019, 12, 03));
        return film;
    }

    static Film createTestFilm(Long id) {
        LinkedHashSet<Genre> setGenres = new LinkedHashSet<>();
        setGenres.add(testGenre());
        Film film = new Film();
        if (id != 0) {
            film.setId(id);
        }
        film.setDescription("Описание к фильму");
        film.setName("Фильм новый");
        film.setMpa(testRating());
        film.setGenres(setGenres);
        film.setDuration(113);
        film.setReleaseDate(LocalDate.of(2019, 12, 03));

        return film;
    }

    @Test
    @DisplayName("должен находиться фильм по ID")
    public void shouldReturnFilmById() {
        Optional<Film> userOptional = filmRepository.getFilm(1L);
        Film film = userOptional.get();
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(compareTestFilm());
    }

    @Test
    @DisplayName("должен быть создан Film")
    public void shouldCreateFilm() {
        Film filmNew = filmRepository.addFilm(createTestFilm(0L));

        assertThat(filmNew)
                .usingRecursiveComparison()
                .isEqualTo(createTestFilm(2L));
    }

    @Test
    @DisplayName("должен обновить название фильма")
    public void shouldUpdateFilm() {
        Optional<Film> filmOptional = filmRepository.getFilm(1L);
        Film filmNewName = filmOptional.get();
        filmNewName.setName("Новое_Имя_Фильма");
        filmRepository.updateFilm(filmNewName);
        filmOptional = filmRepository.getFilm(1L);

        assertThat(filmOptional.get().getName())
                .isEqualTo("Новое_Имя_Фильма");
    }

    @Test
    @DisplayName("должен поставиться лайк фильму")
    public void shouldSetLikeToFilm() {
        filmRepository.setLike(1L, 1L);

        List<Film> filmList = filmRepository.findPopular(1).stream().toList();
        assertThat(filmList.get(0).getId())
                .isEqualTo(1);

    }
}