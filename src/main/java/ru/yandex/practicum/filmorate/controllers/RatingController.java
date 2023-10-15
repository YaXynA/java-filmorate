package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.services.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public List<Rating> getAllRatings() {
        log.info("вызван метод для возврата рейтинга");
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public Rating get(@PathVariable("id") int id) {
        log.info("вызван метод возврат рейтига по id");
        return ratingService.getRatingById(id);
    }
}