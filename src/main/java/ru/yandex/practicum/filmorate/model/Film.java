package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Month;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;

    @NotBlank(message = "Наименование не должно быть пустым")
    private String name;

    @NotBlank(message = "Описание не должно быть пустым")
    @Size(max = 200, message = "Описание должно содержать не более {max} символов")
    private String description;
    private LocalDate releaseDate;

    @Positive(message = "Длительность должна быть положительным числом")
    private int duration;

    @AssertTrue(message = "Дата выпуска должна быть после 28 декабря 1895 года")
    public boolean isValidReleaseDate() {
        if (releaseDate == null) {
            return false;
        }
        return releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));
    }
}