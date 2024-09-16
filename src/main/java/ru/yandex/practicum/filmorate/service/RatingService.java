package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public Collection<Rating> getRatingList() {
        return ratingRepository.getRatingList();
    }

    public Rating findById(long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден"));
    }
}
