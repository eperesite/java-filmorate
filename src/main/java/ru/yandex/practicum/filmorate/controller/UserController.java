package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        log.info("Добавление нового пользователя: {}", user);
        User createdUser = userService.addUser(user);
        log.info("Пользователь успешно добавлен: {}", createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        User updatedUser = userService.updateUser(user);
        log.info("Пользователь успешно обновлен: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Получение всех пользователей");
        List<User> usersList = userService.getUsers();
        log.info("Получено {} пользователей", usersList.size());
        return ResponseEntity.ok(usersList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        log.info("Получение пользователя с ID: {}", id);
        User user = userService.getUser(id);
        log.info("Получен пользователь: {}", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Добавление в друзья пользователя {} к пользователю {}", friendId, userId);
        userService.addFriend(userId, friendId);
        log.info("Пользователь {} успешно добавлен в друзья пользователю {}", friendId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Удаление из друзей пользователя {} от пользователя {}", friendId, userId);
        userService.removeFriend(userId, friendId);
        log.info("Пользователь {} успешно удален из друзей пользователя {}", friendId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable int userId) {
        log.info("Получение списка друзей пользователя {}", userId);
        List<User> friends = userService.getFriends(userId);
        log.info("Получено {} друзей пользователя {}", friends.size(), userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable int userId, @PathVariable int otherUserId) {
        log.info("Получение общих друзей между пользователем {} и пользователем {}", userId, otherUserId);
        List<User> commonFriends = userService.getCommonFriends(userId, otherUserId);
        log.info("Получено {} общих друзей между пользователем {} и пользователем {}", commonFriends.size(), userId, otherUserId);
        return ResponseEntity.ok(commonFriends);
    }
}
