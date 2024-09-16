package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;

    public User addUser(User user) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        final String INSERT_QUERY = "INSERT INTO USERS (USER_NAME,EMAIL,LOGIN,BIRTHDAY) " +
                "VALUES(:u_name,:u_email,:u_login,:u_bday)";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("u_name", user.getName());
        mapSqlParameterSource.addValue("u_email", user.getEmail());
        mapSqlParameterSource.addValue("u_login", user.getLogin());
        mapSqlParameterSource.addValue("u_bday", user.getBirthday());

        jdbc.update(INSERT_QUERY, mapSqlParameterSource, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    public User updateUser(User newUser) {
        final String UPDATE_QUERY = "UPDATE USERS SET USER_NAME = :u_name, EMAIL = :u_email, LOGIN = :u_login, BIRTHDAY = :u_bday" +
                " WHERE USER_ID = :u_id";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("u_id", newUser.getId());
        mapSqlParameterSource.addValue("u_name", newUser.getName());
        mapSqlParameterSource.addValue("u_email", newUser.getEmail());
        mapSqlParameterSource.addValue("u_login", newUser.getLogin());
        mapSqlParameterSource.addValue("u_bday", newUser.getBirthday());

        jdbc.update(UPDATE_QUERY, mapSqlParameterSource);
        return newUser;
    }

    public Collection<User> getUsers() {
        final String SELECT_QUERY = "SELECT * FROM USERS"; //+dB
        return jdbc.query(SELECT_QUERY, new UserRowMapper());
    }

    public Optional<User> getUser(long id) {
        final String SELECT_QUERY = "SELECT * FROM USERS WHERE USER_ID = :u_id";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("u_id", id);
        try {
            return Optional.ofNullable(jdbc.queryForObject(SELECT_QUERY, mapSqlParameterSource, new UserRowMapper()));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }


    public void addFriend(User user, User friend) {
        final String INSERT_QUERY = "INSERT INTO FRIENDS (USER_ID,FRIEND_ID) VALUES(:u_id,:f_id)"; //+dB
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("u_id", user.getId());
        mapSqlParameterSource.addValue("f_id", friend.getId());

        if (checkExistUSERFrends(user.getId(), friend.getId())) {
            jdbc.update(INSERT_QUERY, mapSqlParameterSource);
        }
    }


    public void deleteFriend(User user, User friend) {
        final String DELETE_QUERY = "DELETE FROM FRIENDS WHERE USER_ID = :u_id AND FRIEND_ID = :f_id"; //+ dB
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("u_id", user.getId());
        mapSqlParameterSource.addValue("f_id", friend.getId());
        jdbc.update(DELETE_QUERY, mapSqlParameterSource);
    }

    public List<User> getFriends(User user) {
        final String SELECT_QUERY = "SELECT * FROM USERS AS u INNER JOIN FRIENDS AS uf  ON uf.FRIEND_ID = u.USER_ID WHERE uf.USER_ID = :u_id "; //+dB
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("u_id", user.getId());
        return jdbc.query(SELECT_QUERY, mapSqlParameterSource, new UserRowMapper());
    }

    public List<User> getMutualFriends(User user, User otherUser) {
        final String SELECT_QUERY = "SELECT * FROM USERS " +
                " JOIN FRIENDS AS USFirst ON USFirst.FRIEND_ID  = USERS.USER_ID" +
                " JOIN FRIENDS AS USSecond" +
                " ON USSecond.FRIEND_ID = USFirst.FRIEND_ID" +
                " WHERE USFirst.USER_ID = :uFirst_id AND USSecond.USER_ID = :uSecond_id";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("uFirst_id", user.getId());
        mapSqlParameterSource.addValue("uSecond_id", otherUser.getId());

        return jdbc.query(SELECT_QUERY, mapSqlParameterSource, new UserRowMapper());
    }

    private boolean checkExistUSERFrends(Long userId, Long friendId) {
        boolean ret = true;
        final String SELECT_QUERY = "SELECT count(*) FROM FRIENDS WHERE USER_ID = :u_id AND FRIEND_ID = :f_id"; //+dB

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("f_id", friendId);
        mapSqlParameterSource.addValue("u_id", userId);

        if (jdbc.queryForObject(SELECT_QUERY, mapSqlParameterSource, Integer.class) != 0)
            ret = false;

        return ret;
    }
}