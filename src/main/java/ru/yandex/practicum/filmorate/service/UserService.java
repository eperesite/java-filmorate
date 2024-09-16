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
public class UserService {
    private final UserRepository userRepository;

    public Collection<User> getAllUsers() {
        return userRepository.getUsers();
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public User updateUser(User user) {
        userRepository.getUser(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return userRepository.updateUser(user);
    }

    public User getUser(long id) {
        return userRepository.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public List<User> getFriends(long id) {
        return userRepository.getFriends(getUser(id));
    }

    public void addFriend(long id, long friendId) {
        userRepository.addFriend(getUser(id), getUser(friendId));
    }

    public void removeFriend(long id, long friendId) {
        userRepository.deleteFriend(getUser(id), getUser(friendId));
    }

    public List<User> getCommonFriends(long id, long otherId) {
        return userRepository.getMutualFriends(getUser(id), getUser(otherId));
    }
}
