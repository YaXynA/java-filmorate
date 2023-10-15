package ru.yandex.practicum.filmorate.storages.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.GenreStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class FilmDao implements FilmStorage {

    int id = 1;
    private final JdbcTemplate jdbcTemplate;

    private final GenreStorage genreStorage;


    @Override
    public Film save(Film film) {
        String sql = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) VALUES (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((PreparedStatementCreator) connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());
        log.info("Добавлен фильм с Id: " + film.getId() + " и названием: " + film.getName());

        if (!film.getGenres().isEmpty()) {
            String sqlUpdate = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlUpdate, film.getId(), genre.getId());
            }
        }

        film.setGenres(genreStorage.getGenreByFilmId(film.getId()));

        return film;
    }

    @Override
    public Film update(Film film) {

        get(film.getId());

        Integer ratingId = film.getMpa().getId();

        String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), ratingId, film.getId());

        String genreClearSQL = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        if (film.getGenres().size() == 0) {
            jdbcTemplate.update(genreClearSQL, film.getId());
        } else {
            jdbcTemplate.update(genreClearSQL, film.getId());
            String sqlUpdate = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlUpdate, film.getId(), genre.getId());
            }
        }

        film.setGenres(genreStorage.getGenreByFilmId(film.getId()));

        log.info("Обновлен фильм с Id: " + film.getId() + " и названием: " + film.getName());
        return film;
    }

    @Override
    public void deleteById(int id) {

        Film film = get(id);
        if (film.getId() != 0) {
            String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
            jdbcTemplate.update(sql, id);
            log.info("Удален фильм с Id: " + film.getId());
        } else {
            throw new NotFoundException("Не найден фильм с id: " + id);
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.RATING_NAME FROM FILMS f JOIN RATING r \n" +
                "\tON f.RATING_ID  = r.RATING_ID ";

        try {
            return new ArrayList<>(jdbcTemplate.query(sql, this::mapRowToFilm));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }

    }

    @Override
    public Film get(int id) {

        String sql = "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.RATING_NAME FROM FILMS f JOIN RATING r \n" +
                "\tON f.RATING_ID  = r.RATING_ID " +
                "WHERE f.FILM_ID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Не найден фильм с id: " + id);
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {

        UserDao userDBStorage = new UserDao(jdbcTemplate);

        try {
            userDBStorage.get(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Не был найден юзер с Id = " + id);
        }

        String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getFilmsByCount(int count) {


        String sql = "SELECT DISTINCT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.RATING_NAME , COALESCE (fg.LIKES,0) AS LIKES\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN RATING r ON f.RATING_ID  = r.RATING_ID\n" +
                "LEFT JOIN FILMS_GENRES fg ON fg.FILM_ID = f.FILM_ID \n" +
                "LEFT JOIN (SELECT FILM_ID AS fid, count(LIKE_ID) AS LIKES FROM LIKES \n" +
                "GROUP BY FILM_ID) fg ON f.FILM_ID  = fg.fid\n" +
                "ORDER BY LIKES DESC LIMIT " + count;


        return new ArrayList<>(jdbcTemplate.query(sql, this::mapRowToFilm));
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film
                .builder()
                .id(rs.getInt("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getLong("DURATION"))
                .mpa(new Rating(rs.getInt("RATING_ID"), rs.getString("RATING_NAME")))
                .genres(genreStorage.getGenreByFilmId(rs.getInt("FILM_ID")))
                .build();
    }


}