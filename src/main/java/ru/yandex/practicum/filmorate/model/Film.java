package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Film {

    private Integer id;

    @NotBlank(message = "Название не может быть пустым!")
    private String name;

    @Size(min = 1,max = 200, message = "Максимальная длина описания — 200 символов!")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ReleaseDate
    private LocalDate releaseDate;

    @Min(value = 0, message = "Продолжительность фильма должна быть положительной!")
    private Long duration;

    private Rating mpa;

    private final Set<Integer> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();

    public void addLike(int id) {
        likes.add(id);
    }

    public void deleteLike(int id) {
        likes.remove(id);
    }

    public int getLikesCount() {
        return likes.size();
    }

}