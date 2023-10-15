package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private int filmId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    private int setFilmId() {
        return filmId++;
    }

    @Autowired
    private InMemoryUserStorage userStorage;

    private final Comparator<Film> filmComparator = Comparator.comparing(Film::getLikesCount, Comparator.reverseOrder());

    @Override
    public Film save(Film film) {
        int id = setFilmId();
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен фильм с Id: " + film.getId() + " и названием: " + film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.get(film.getId()) != null) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм с Id: " + film.getId() + " и названием: " + film.getName());
            return film;
        } else {
            throw new NotFoundException("Не найден фильм с id: " + film.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        if (films.get(id) != null) {
            films.remove(id);
        } else {
            throw new NotFoundException("Не найден фильм с id: " + id);
        }
    }

    @Override
    public List<Film> getAll() {
        return List.copyOf(films.values());
    }

    @Override
    public Film get(int id) {
        if (films.get(id) != null) {
            return films.get(id);
        } else {
            throw new NotFoundException("Не найден фильм с id: " + id);
        }
    }

    @Override
    public void addLike(int fId, int uId) {
        if (userStorage.get(uId) != null) {
            if (films.get(fId) != null) {
                films.get(fId).addLike(uId);
            } else {
                throw new NotFoundException("Не найден фильм с id: " + fId);
            }
        } else {
            throw new NotFoundException("Не найден user с id: " + uId);
        }
    }

    @Override
    public void deleteLike(int fId, int uId) {
        if (userStorage.get(uId) != null) {
            if (films.get(fId) != null) {
                films.get(fId).deleteLike(uId);
            } else {
                throw new NotFoundException("Не найден фильм с id: " + fId);
            }
        } else {
            throw new NotFoundException("Не найден user с id: " + uId);
        }
    }

    @Override
    public List<Film> getFilmsByCount(int count) {
        return films
                .values()
                .stream()
                .sorted(filmComparator)
                .limit(count)
                .collect(Collectors.toList());
    }
}