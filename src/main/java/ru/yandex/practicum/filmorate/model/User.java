package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String login;
    private String name;

    @PastOrPresent
    private LocalDate birthday;
}