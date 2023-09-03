package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("Вызван запрос POST /films");
        film.setId(nextFilmId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Вызван запрос PUT /films");
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.warn("Фильм с указанным ID не найден {}", film.getId());
            throw new ValidationException("Фильм с указанным id не найден");
        }
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.debug("Вызван запрос GET /films");
        return new ArrayList<>(films.values());
    }

    private int nextFilmId() {
        return filmId++;
    }
}