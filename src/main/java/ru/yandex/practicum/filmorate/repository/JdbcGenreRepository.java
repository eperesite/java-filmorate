package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Collection<Genre> getGenres() {
        final String SELECT_QUERY = "SELECT GENRE_ID,GENRE_NAME FROM GENRES"; //+dB
        return jdbc.query(SELECT_QUERY, new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(Long id) {
        final String SELECT_QUERY = "SELECT GENRE_ID,GENRE_NAME FROM GENRES WHERE GENRE_ID = :genreId"; //+dB
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("genreId", id);

        try {
            return Optional.ofNullable(jdbc.queryForObject(SELECT_QUERY, mapSqlParameterSource, new GenreRowMapper()));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findByIds(List<Long> genreIds) {
        final String SELECT_QUERY_IN = "SELECT GENRE_ID,GENRE_NAME FROM GENRES WHERE GENRE_ID IN (:g_Ids)"; //+dB

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("g_Ids", genreIds);
        return jdbc.query(SELECT_QUERY_IN, mapSqlParameterSource, new GenreRowMapper());
    }

    @Override
    public Genre create(Genre genre) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSER_GENRE_QUERY = "INSERT INTO GENRES (GENRE_NAME) values (g_name)"; //+dB
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("g_id", genre);

        jdbc.update(INSER_GENRE_QUERY, mapSqlParameterSource, keyHolder);
        genre.setId(keyHolder.getKeyAs(Long.class));
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        final String UPDATE_GENRE_QUERY = "UPDATE GENRES SET GENRE_NAME = :g_name WHERE GENRE_ID = :g_id"; //+dB
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("g_id", genre.getId());
        mapSqlParameterSource.addValue("g_name", genre.getName());
        jdbc.update(UPDATE_GENRE_QUERY, mapSqlParameterSource);

        return genre;
    }

    @Override
    public void delete(Genre genre) {
        final String DELETE_GENRE_QUERY = "DELETE FROM GENRES WHERE GENRE_ID = :g_id"; //+ dB
        final String DELETE_FILMGENRES_QUERY = "DELETE FROM FILMSGENRES WHERE GENRE_ID = :g_id"; //+dB

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("g_id", genre.getId());

        jdbc.update(DELETE_GENRE_QUERY, mapSqlParameterSource);
        jdbc.update(DELETE_FILMGENRES_QUERY, mapSqlParameterSource);
    }
}