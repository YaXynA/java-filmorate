package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Set;

public interface FilmGenreStorage {

    void addGenreForFilm(int filmId, int genreId);

    Set<FilmGenre> getFilmGenres(int filmId);

}