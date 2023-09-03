package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Вызван запрос POST /users");
        checkUserName(user);
        user.setId(generateUserId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        log.debug("Вызван запрос PUT /users");
        if (users.containsKey(user.getId())) {
            checkUserName(user);
            users.put(user.getId(), user);
            return ResponseEntity.ok(user);
        } else {
            log.warn("Пользователь с указанным id не найден {}", user.getId());
            String errorMessage = "Пользователь с указанным id не найден";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @GetMapping
    public List<User> getUsers() {
        log.debug("Вызван запрос GET /users");
        return new ArrayList<>(users.values());
    }

    private int generateUserId() {
        return nextUserId++;
    }

    private void checkUserName(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}