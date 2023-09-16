package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User save(@Valid @RequestBody User user) {
        log.info("вызван запрос save пользователь = {}", user);
        return userService.saveUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("вызван запрос пользователь update = {}", user);
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("вызван запрос getUsers");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") int id) {
        log.info("вызван запрос get userid");
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("вызван запрос delete userid");
        userService.deleteById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("вызван запрос addFriend user id = " + id + "friend id = " + friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("вызван запрос deleteFriend user id = " + id + "friend id = " + friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int id) {
        log.info("вызван запрос getFriends id = " + id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        log.info("вызван запрос getCommonFriends Userid = " + id + " other id = " + otherId);
        return userService.getCommonFriends(id, otherId);
    }

}