package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

    public User saveUser(User user) {
        return inMemoryUserStorage.save(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.update(user);
    }

    public List<User> getUsers() {
        return inMemoryUserStorage.getAll();
    }

    public User getById(int id) {
        return inMemoryUserStorage.get(id);
    }

    public void deleteById(int id) {
        inMemoryUserStorage.deleteById(id);
    }

    public void addFriend(int id, int friendId) {
        inMemoryUserStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        inMemoryUserStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(int id) {
        return inMemoryUserStorage.getFriends(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return inMemoryUserStorage.getCommonFriends(id, otherId);
    }

}