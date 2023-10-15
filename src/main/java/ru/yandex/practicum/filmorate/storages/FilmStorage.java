package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film save(Film film);

    Film update(Film film);

    void deleteById(int id);

    List<Film> getAll();

    Film get(int id);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getFilmsByCount(int count);

}