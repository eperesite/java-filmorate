package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public User addUser(User user);

    User updateUser(User newUser);

    Collection<User> getUsers();

    Optional<User> getUser(long id);


    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getFriends(User user);

    List<User> getMutualFriends(User user, User otherUser);
}