package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FilmService {

    @Autowired
    InMemoryFilmStorage inMemoryFilmStorage;

    public Film saveFilm(Film film) {
        return inMemoryFilmStorage.save(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.update(film);
    }

    public List<Film> getFilms() {
        return inMemoryFilmStorage.getAll();
    }

    public Film getById(int id) {
        return inMemoryFilmStorage.get(id);
    }

    public void deleteById(int id) {
        inMemoryFilmStorage.deleteById(id);
    }

    public void putLike(int filmId, int userId) {
        inMemoryFilmStorage.putLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        inMemoryFilmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getFilmsByCount(int count) {
        return inMemoryFilmStorage.getFilmsByCount(count);
    }

}