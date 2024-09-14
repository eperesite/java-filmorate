package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

@Service
public interface RatingService {
    Collection<Rating> getRatingList();

    Rating findById(long id);
}