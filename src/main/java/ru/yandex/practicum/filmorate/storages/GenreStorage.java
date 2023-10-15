package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    List<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    Set<Genre> getGenreByFilmId(int filmId);
}