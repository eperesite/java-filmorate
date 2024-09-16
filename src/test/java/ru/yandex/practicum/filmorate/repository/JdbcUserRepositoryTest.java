package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import(JdbcUserRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcUserRepository")
class JdbcUserRepositoryTest {
    private final UserRepository userRepository;
    public static final long FindUser_Id = 1L;

    static User compareTestUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("test@mail.com");
        user.setLogin("UserName");
        user.setName("UserName");
        user.setBirthday(LocalDate.of(1990, 10, 20));
        return user;
    }

    static User createTestUser() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("UserName");
        user.setName("UserName");
        user.setBirthday(LocalDate.of(1990, 10, 20));
        return user;
    }

    @Test
    @DisplayName("должен находиться пользователь по ID")
    public void shouldReturnUserById() {

        Optional<User> userOptional = userRepository.getUser(FindUser_Id);

        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(compareTestUser(FindUser_Id));
    }

    @Test
    @DisplayName("должен обновить имя пользователя")
    public void shouldUpdateUser() {
        Optional<User> userOptional = userRepository.getUser(1);
        User userNewName = userOptional.get();
        userNewName.setName("Новое_Имя");
        userRepository.updateUser(userNewName);
        userOptional = userRepository.getUser(1);

        assertThat(userOptional.get().getName())
                .isEqualTo("Новое_Имя");
    }

    @Test
    @DisplayName("должен быть получен списко всех пользователей и размер списка должен соответсвовать кол-ву Users в BD")
    public void shouldDeleteUser() {
        List<User> listUsers = userRepository.getUsers().stream().toList();
        assertThat(listUsers.size())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("должен быть добавлен friend (размер спика друзей должен быть 1)")
    public void shouldAddFriendToUser() {
        Optional<User> userOptional = userRepository.getUser(1);
        Optional<User> friendOptional = userRepository.getUser(2);
        User user = userOptional.get();
        User friend = friendOptional.get();

        userRepository.addFriend(user, friend);

        List<User> listFriends = userRepository.getFriends(user);
        assertThat(listFriends.size())
                .isEqualTo(1);
    }
}