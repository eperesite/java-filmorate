package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.mappers.RatingRowMapper;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcRatingRepository implements RatingRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Rating> findById(Long id) {
        final String SELECT_QUERY = "SELECT * FROM RATING WHERE RATING_ID = :ratingId"; //+dB
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ratingId", id);
        try {
            return Optional.ofNullable(jdbc.queryForObject(SELECT_QUERY, mapSqlParameterSource, new RatingRowMapper()));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Rating> getRatingList() {
        final String SELECT_QUERY = "SELECT RATING_ID, RATING_NAME FROM RATING"; //+dB
        return jdbc.query(SELECT_QUERY, new RatingRowMapper());
    }

    @Override
    public Rating create(Rating rating) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSER_GENRE_QUERY = "INSERT INTO RATING (RATING_NAME) values (r_name)"; //+dB
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("r_id", rating);

        jdbc.update(INSER_GENRE_QUERY, mapSqlParameterSource, keyHolder);
        rating.setId(keyHolder.getKeyAs(Long.class));
        return rating;
    }

    @Override
    public void delete(Rating rating) {
        final String DELETE_RATING_QUERY = "DELETE FROM RATING WHERE RATING_ID = :r_id"; //+ dB

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("r_id", rating.getId());

        jdbc.update(DELETE_RATING_QUERY, mapSqlParameterSource);
    }

    @Override
    public Rating update(Rating rating) {
        final String UPDATE_GENRE_QUERY = "UPDATE RATING SET RATING_NAME = :g_name WHERE RATING_ID = :g_id"; //+dB

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("r_id", rating.getId());
        mapSqlParameterSource.addValue("r_name", rating.getName());

        jdbc.update(UPDATE_GENRE_QUERY, mapSqlParameterSource);

        return rating;
    }
}