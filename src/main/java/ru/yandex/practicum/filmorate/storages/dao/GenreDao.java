package ru.yandex.practicum.filmorate.storages.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storages.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenreDao implements GenreStorage { //убрал конструтор

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT GENRE_ID, GENRE_NAME FROM GENRES");

        while (genreRows.next()) {
            Genre genre = Genre
                    .builder()
                    .id(genreRows.getInt("GENRE_ID"))
                    .name(genreRows.getString("GENRE_NAME"))
                    .build();
            genres.add(genre);
        }

        return genres;
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sql = "SELECT GENRE_ID, GENRE_NAME FROM GENRES WHERE GENRE_ID = ? order by GENRE_ID asc";

        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        } catch (RuntimeException e) {
            throw new NotFoundException("Жанр с id = " + id + " не найден в справочнике жанров");
        }
    }

    @Override
    public Set<Genre> getGenreByFilmId(int filmId) {
        String sql = "SELECT g.GENRE_ID, g.GENRE_NAME  \n" +
                "\tFROM FILMS_GENRES fg \n" +
                "\tJOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID  \n" +
                "\tWHERE fg.FILM_ID  = ? " +
                " order by fg.GENRE_ID asc";

        List<Genre> genres = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")),
                filmId
        );
        return new HashSet<>(genres);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre
                .builder()
                .id(rs.getInt("GENRE_ID"))
                .name(rs.getString("GENRE_NAME"))
                .build();
    }
}