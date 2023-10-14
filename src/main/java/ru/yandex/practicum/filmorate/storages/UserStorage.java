package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User save(User user);

    User update(User user);

    void deleteById(int id);

    List<User> getAll();

    User get(int id);

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<User> getFriends(int id);

    List<User> getCommonFriends(int id, int otherId);
}
