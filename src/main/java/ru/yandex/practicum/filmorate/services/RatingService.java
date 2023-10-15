package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storages.RatingStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingStorage ratingStorage;

    public List<Rating> getAllRatings() {
        return ratingStorage.getAllRatings();
    }

    public Rating getRatingById(Integer id) {
        return ratingStorage.getRatingById(id);
    }
}