package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Component
public class FilmExtractorWithoutGenre implements ResultSetExtractor<Map<Long, Film>> {
    @Override
    public Map<Long, Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> data = new LinkedHashMap<>();
        Film film = null;

        while (rs.next()) {
            Long id = rs.getLong("f_id");
            film = data.get(id);

            if (film == null) {
                film = new Film();
                film.setId(rs.getLong("f_id"));
                film.setName(rs.getString("f_name"));
                film.setDescription(rs.getString("f_descr"));
                film.setReleaseDate(rs.getDate("f_rDate").toLocalDate());
                film.setDuration(rs.getInt("f_dur"));

                Rating rating = new Rating();
                rating.setId(rs.getLong("r_id"));
                rating.setName(rs.getString("r_name"));
                film.setMpa(rating);
                film.setGenres(new LinkedHashSet<Genre>());
            }
            data.put(film.getId(), film);
        }
        return data;
    }
}