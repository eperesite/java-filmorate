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

    public Collection<User> getUsersList() {
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

    public User getUserById(long id) {
        return userRepository.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public List<User> getFriends(long id) {
        return userRepository.getFriends(getUserById(id));
    }

    public void addFriend(long id, long friendId) {
        userRepository.addFriend(getUserById(id), getUserById(friendId));
    }

    public void deleteFriend(long id, long friendId) {
        userRepository.deleteFriend(getUserById(id), getUserById(friendId));
    }

    public List<User> getMutualFriends(long id, long otherId) {
        return userRepository.getMutualFriends(getUserById(id), getUserById(otherId));
    }
}
