package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.repository.mappers.FilmExtractorWithoutGenre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Film addFilm(Film film) {
        Long ratingId = 0L;
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        LinkedHashSet<Genre> genreSet;

        final String INSERT_QUERY = "INSERT INTO FILMS (FILM_NAME,DESCRIPTION,RATING_ID,RELEASE_DATE,DURATION)" +
                " VALUES(:f_name,:f_desc,:f_rating,:f_rDate,:f_dur)";
        if (film.getMpa() != null) {
            ratingId = film.getMpa().getId();
        }
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("f_name", film.getName());
        mapSqlParameterSource.addValue("f_desc", film.getDescription());
        mapSqlParameterSource.addValue("f_rDate", film.getReleaseDate());
        mapSqlParameterSource.addValue("f_dur", film.getDuration());
        mapSqlParameterSource.addValue("f_rating", ratingId);

        jdbc.update(INSERT_QUERY, mapSqlParameterSource, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        if (film.getGenres() != null) {
            genreSet = film.getGenres();

            for (Genre genreOfFilm : genreSet) {
                createFILMSGENRES(film.getId(), genreOfFilm.getId());
            }
        }
        return film;
    }

    public Film updateFilm(Film film) {
        final String UPDATE_QUERY = "UPDATE FILMS SET FILM_NAME = :f_name, RATING_ID = :f_rating, " +
                "DESCRIPTION = :f_desc, RELEASE_DATE = :f_rDate, DURATION = :f_dur " +
                " WHERE FILM_ID = :f_id";
        final String DELETE_FILMGENRES_QUERY = "DELETE FROM FILMSGENRES WHERE FILM_ID = :f_id";
        final String INSER_FILMGENRES_QUERY = "INSERT INTO  FILMSGENRES (FILM_ID, GENRE_ID) values (:f_id, :g_id)";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("f_id", film.getId());
        mapSqlParameterSource.addValue("f_name", film.getName());
        mapSqlParameterSource.addValue("f_rating", film.getMpa().getId());
        mapSqlParameterSource.addValue("f_desc", film.getDescription());
        mapSqlParameterSource.addValue("f_rDate", film.getReleaseDate());
        mapSqlParameterSource.addValue("f_dur", film.getDuration());

        jdbc.update(UPDATE_QUERY, mapSqlParameterSource);
        jdbc.update(DELETE_FILMGENRES_QUERY, mapSqlParameterSource);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                createFILMSGENRES(film.getId(), genre.getId());
            }
        }
        return film;
    }

    public List<Film> getFilms() {
        final String SELECT_QUERY = "SELECT f.FILM_ID as f_id, f.FILM_NAME as f_name,f.DESCRIPTION as f_descr,f.RELEASE_DATE as f_rDate,f.DURATION as f_dur" +
                ",g.GENRE_ID as g_id, g.GENRE_NAME as g_name,r.RATING_ID as r_id, r.RATING_NAME as r_name" +
                " FROM FILMS as f" +
                " LEFT OUTER JOIN FILMSGENRES as fg on fg.FILM_ID = f.FILM_ID" +
                " RIGHT OUTER JOIN GENRES AS g ON g.GENRE_ID = fg.GENRE_ID" +
                " LEFT OUTER JOIN RATING AS r ON r.RATING_ID = f.RATING_ID" +
                " ORDER BY f.FILM_ID";
        Map<Long, Film> mapFilms = new LinkedHashMap<Long, Film>();
        mapFilms = jdbc.query(SELECT_QUERY, new FilmExtractor());
        return new ArrayList<Film>(mapFilms.values());
    }

    public Collection<Film> findPopular(int count) {
        final String SELECT_QUERY = "SELECT f.FILM_ID as f_id, f.FILM_NAME as f_name,f.DESCRIPTION as f_descr,f.RELEASE_DATE as f_rDate,f.DURATION as f_dur," +
                "COUNT(fl.USER_ID),r.RATING_ID as r_id, r.RATING_NAME as r_name" +
                " FROM FILMS AS f" +
                " INNER JOIN  LIKES as fl ON f.FILM_ID = fl.FILM_ID" +
                " JOIN RATING AS r ON r.RATING_ID = f.RATING_ID" +
                " GROUP BY fl.FILM_ID ,f.RATING_ID" +
                " ORDER BY COUNT(USER_ID) DESC LIMIT :count";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("count", count);

        Map<Long, Film> mapFilms = jdbc.query(SELECT_QUERY, mapSqlParameterSource, new FilmExtractorWithoutGenre());
        return new ArrayList<Film>(mapFilms.values());
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        final String SELECT_QUERY = "SELECT f.FILM_ID as f_id, f.FILM_NAME as f_name,f.DESCRIPTION as f_descr,f.RELEASE_DATE as f_rDate,f.DURATION as f_dur" +
                ",g.GENRE_ID as g_id, g.GENRE_NAME as g_name,r.RATING_ID as r_id, r.RATING_NAME as r_name" +
                " FROM FILMS as f" +
                " LEFT OUTER JOIN FILMSGENRES as fg on fg.FILM_ID = f.FILM_ID" +
                " LEFT OUTER JOIN GENRES AS g ON g.GENRE_ID = fg.GENRE_ID" +
                " LEFT OUTER JOIN RATING AS r ON r.RATING_ID = f.RATING_ID" +
                " WHERE f.FILM_ID = :filmId" +
                " ORDER BY f.FILM_ID";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("filmId", id);

        try {
            return Optional.ofNullable(jdbc.query(SELECT_QUERY, mapSqlParameterSource, new FilmExtractor()).get(id));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public void setLike(Long filmId, Long userId) {
        final String INSERT_QUERY = "INSERT INTO LIKES (FILM_ID,USER_ID) " +
                "VALUES(:f_id,:u_id)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("f_id", filmId);
        mapSqlParameterSource.addValue("u_id", userId);

        if (!checkExistFILMLIKES(filmId, userId)) {
            jdbc.update(INSERT_QUERY, mapSqlParameterSource);
        }
        boolean ret = checkExistFILMLIKES(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        final String DELETE_QUERY = "DELETE FROM LIKES WHERE FILM_ID = :f_id AND USER_ID = :u_id";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("f_id", filmId);
        mapSqlParameterSource.addValue("u_id", userId);
        jdbc.update(DELETE_QUERY, mapSqlParameterSource);
    }

    private void createFILMSGENRES(Long filmId, Long genreId) {
        final String INSERT_QUERY = "INSERT INTO FILMSGENRES (FILM_ID,GENRE_ID)" +   //+dB
                " VALUES(:f_id,:g_id)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("f_id", filmId);
        mapSqlParameterSource.addValue("g_id", genreId);

        if (!checkExistFilmRange(filmId, genreId)) {
            jdbc.update(INSERT_QUERY, mapSqlParameterSource);
        }
    }

    private Boolean checkExistFilmRange(Long filmId, Long genreId) {
        boolean ret = false;
        final String SELECT_QUERY = "SELECT count(*) FROM FILMSGENRES WHERE FILM_ID = :f_id AND GENRE_ID = :g_id";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("f_id", filmId);
        mapSqlParameterSource.addValue("g_id", genreId);

        if (jdbc.queryForObject(SELECT_QUERY, mapSqlParameterSource, Integer.class) != 0) {
            ret = true;
        }
        return ret;
    }


    private boolean checkExistFILMLIKES(Long filmId, Long userId) {
        boolean ret = false;
        final String SELECT_QUERY = "SELECT count(*) FROM LIKES WHERE FILM_ID = :f_id AND USER_ID = :u_id";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("f_id", filmId);
        mapSqlParameterSource.addValue("u_id", userId);

        if (jdbc.queryForObject(SELECT_QUERY, mapSqlParameterSource, Integer.class) != 0) {
            ret = true;
        }
        return ret;
    }
}