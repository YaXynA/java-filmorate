package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User saveUser(User user) {
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public User getById(int id) {
        return userStorage.get(id);
    }

    public void deleteById(int id) {
        userStorage.deleteById(id);
    }

    public void addFriend(int id, int friendId) {
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}