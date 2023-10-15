package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FilmGenre {

    private Integer filmGenreId;
    private Integer filmId;
    private Integer genreId;

}