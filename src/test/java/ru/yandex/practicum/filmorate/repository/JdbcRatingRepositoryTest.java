package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import(JdbcRatingRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcRatingRepository")
public class JdbcRatingRepositoryTest {
    private final JdbcRatingRepository ratingRepository;

    @Test
    @DisplayName("должен получить список всех Mpa")
    public void shouldGetAllMPA() {
        List<Rating> listMPA = ratingRepository.getRatingList().stream().toList();


        assertThat(listMPA.size())
                .isEqualTo(5);
    }
}