package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<User> getUsersList() {
        return userRepository.getUsers();
    }

    @Override
    public User addUser(User newUser) {
        return userRepository.addUser(newUser);
    }

    @Override
    public User updateUser(User newUser) {
        final User user = userRepository.getUser(newUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с " + newUser.getId() + "не найден"));
        return userRepository.updateUser(newUser);
    }

    @Override
    public User getUserById(long id) {
        final User user = userRepository.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с " + id + "не найден"));
        return user;
    }

    @Override
    public List<User> getFriends(long id) {

        return userRepository.getFriends(getUserById(id));
    }

    @Override
    public void addFriend(long id, long friendId) {
        userRepository.addFriend(getUserById(id), getUserById(friendId));
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        userRepository.deleteFriend(getUserById(id), getUserById(friendId));
    }

    @Override
    public List<User> getMutualFriends(long id, long otherId) {
        final User user = userRepository.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с " + id + "не найден"));
        final User userOther = userRepository.getUser(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователь с " + otherId + "не найден"));
        return userRepository.getMutualFriends(user, userOther);
    }
}