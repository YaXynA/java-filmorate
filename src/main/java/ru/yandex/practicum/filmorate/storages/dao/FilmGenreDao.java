package ru.yandex.practicum.filmorate.storages.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storages.FilmGenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;



@Slf4j
@Component
public class FilmGenreDao implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenreForFilm(int filmId, int genreId) {

        String sql = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, genreId);
        log.info("Добавлен жанр Id: " + genreId + "для фильма с Id: " + filmId);
    }

    @Override
    public Set<FilmGenre> getFilmGenres(int filmId) {
        String sql = "SELECT * FROM FILMS_GENRES WHERE FILM_ID = ?";

        Set<FilmGenre> filmGenres = new HashSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, filmId);

        while ((genreRows.next())) {
            FilmGenre filmGenre = FilmGenre
                    .builder()
                    .filmGenreId(genreRows.getInt("FILM_GENRE_ID"))
                    .filmId(genreRows.getInt("FILM_ID"))
                    .genreId(genreRows.getInt("GENRE_ID"))
                    .build();
            filmGenres.add(filmGenre);
        }

        return filmGenres;
    }

    private FilmGenre mapRowToFilmGenre(ResultSet rs, int rowNum) throws SQLException {
        return FilmGenre
                .builder()
                .filmGenreId(rs.getInt("FILM_GENRE_ID"))
                .filmId(rs.getInt("FILM_ID"))
                .genreId(rs.getInt("GENRE_ID"))
                .build();
    }

}