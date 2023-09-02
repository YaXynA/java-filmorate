package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final List<Film> films = new ArrayList<>();

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Выполнен GET/films");
        return films;
    }

    @PostMapping(value = "/post")
    public Film createFilm(@RequestBody Film film) {
        validateFilm(film);
        films.add(film);
        log.info("Создан новый фильм: {}", film);
        return film;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable int id, @RequestBody Film updatedFilm) {
        validateFilm(updatedFilm);
        Film existingFilm = films.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);
        if (existingFilm != null) {
            existingFilm.setName(updatedFilm.getName());
            existingFilm.setDescription(updatedFilm.getDescription());
            existingFilm.setReleaseDate(updatedFilm.getReleaseDate());
            existingFilm.setDuration(updatedFilm.getDuration());
            log.info("Обновлен фильм с id {}", id);
            return ResponseEntity.ok(existingFilm);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Максимальная длина описания - 200 символов.");
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }

        LocalDate releaseDate = film.getReleaseDate();
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза не может быть раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
