package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {

    private Integer id;

    @NotBlank(message = "Электронная почта не может быть пустой!")
    @Email(message = "Некорректная эл. почта!")
    private String email;

    @NotBlank(message = "Логин не может быть пустым!")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы!")
    private String login;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Дата рождения не может быть в будущем!")
    private LocalDate birthday;

    private final Set<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }

}