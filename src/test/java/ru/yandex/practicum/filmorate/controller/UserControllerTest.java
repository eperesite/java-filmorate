package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    User user;

    ValidatorFactory factory = buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        user = new User(0, "petrov@yandex.ru", "Petrov99", "Petr", LocalDate.of(1999, Month.JUNE, 13));
    }

    @Test
    void create() {
        User newUser = userController.create(user);
        List<User> users = userController.getAll();
        assertEquals(newUser, users.get(0));
    }

    @Test
    void createWithBlankName() {
        user.setName(" ");
        User newUser = userController.create(user);
        List<User> users = userController.getAll();
        assertEquals(user.getLogin(), newUser.getName());
    }

    @Test
    void createWithNullName() {
        user.setName(null);
        User newUser = userController.create(user);
        List<User> users = userController.getAll();
        assertEquals(user.getLogin(), newUser.getName());
    }

    @Test
    void update() {
        User newUser = userController.create(user);
        newUser.setLogin("SuperPetrov99");
        newUser.setEmail("newemail@yandex.ru");
        userController.update(newUser);
        List<User> users = userController.getAll();
        assertEquals(newUser, users.get(0));
    }

    @Test
    void updateWithBlankName() {
        User newUser = userController.create(user);
        newUser.setName(" ");
        userController.update(newUser);
        List<User> users = userController.getAll();
        assertEquals(newUser.getLogin(), users.get(0).getName());
    }

    @Test
    void updateWithNullName() {
        User newUser = userController.create(user);
        newUser.setName(null);
        userController.update(newUser);
        List<User> users = userController.getAll();
        assertEquals(newUser.getLogin(), users.get(0).getName());
    }

    @Test
    void updateNonExistingUser() {
        User nonExistingUser = new User(999, "nonexistent@yandex.ru", "NonExist", "NonExist", LocalDate.of(1990, Month.JANUARY, 1));
        assertThrows(NotFoundException.class, () -> userController.update(nonExistingUser));
    }

    @Test
    void shouldNotValidate() {
        User newUser = new User(0, "petrovyandex.ru@", " ", "Petr", LocalDate.of(2100, Month.JUNE, 13));
        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        assertEquals(3, violations.size());
    }
}