package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Выполнен GET/users");
        return users;
    }

    @PostMapping(value = "/post")
    public User createUser(@RequestBody User user) {
        validateUser(user);

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.add(user);
        log.info("Создан новый пользователь: {}", user);
        return user;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        validateUser(updatedUser);
        User existingUser = users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
        if (existingUser != null) {
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setLogin(updatedUser.getLogin());
            existingUser.setName(updatedUser.getName());
            existingUser.setBirthday(updatedUser.getBirthday());
            log.info("Обновлен пользователь с id {} ", id);
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    private void validateUser(User user) {
        LocalDate birthday = user.getBirthday();
        if (birthday.isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("Некорректная электронная почта.");
            throw new ValidationException("Некорректная электронная почта.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и не должен содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым и не должен содержать пробелы.");
        }
    }
}