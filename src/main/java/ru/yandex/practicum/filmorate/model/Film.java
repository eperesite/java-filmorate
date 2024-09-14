package ru.yandex.practicum.filmorate.model;


import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.LinkedHashSet;


@Data
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Integer likes = 0;
    LinkedHashSet<Genre> genres;
    Rating mpa;
}
