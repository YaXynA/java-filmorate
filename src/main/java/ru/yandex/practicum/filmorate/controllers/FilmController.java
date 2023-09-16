package ru.yandex.practicum.filmorate.controllers;


import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film save(@Valid @RequestBody Film film) {
        log.info("вызван запрос save фильма = {}", film);
        return filmService.saveFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("вызван запрос update фильма  = {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("вызван запрос get фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable("id") int id) {
        log.info("вызван запрос get фильма id");
        return filmService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("вызван запрос delete film id" + id);
        filmService.deleteById(id);
    }


    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("вызван запрос addlike на = " + id + "id user = " + userId);
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("вызван запрос deleteLike = " + id + "id user = " + userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularZeroCount(@RequestParam(defaultValue = "10") int count) {
        log.info("вызван запрос getPopular  = " + count);
        return filmService.getFilmsByCount(count);
    }

}