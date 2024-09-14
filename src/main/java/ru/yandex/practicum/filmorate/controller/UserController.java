package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ValidateService validateService;


    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        validateService.validateUser(user);
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User userNewInfo) {
        validateService.validateUser(userNewInfo);
        return new ResponseEntity<>(userService.updateUser(userNewInfo), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsersList() {
        return new ResponseEntity<>(userService.getUsersList(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public User findUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PutMapping("{id}/friends/{friend-id}")
    public void addFriend(@PathVariable("id") long id, @PathVariable("friend-id") long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friend-id}")
    public void deleteFriend(@PathVariable long id, @PathVariable("friend-id") long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{other-id}")
    public List<User> getMutualFriends(@PathVariable long id, @PathVariable("other-id") long otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
