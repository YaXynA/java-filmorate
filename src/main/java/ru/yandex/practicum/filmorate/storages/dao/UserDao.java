package ru.yandex.practicum.filmorate.storages.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Primary
public class UserDao implements UserStorage {

    int id = 1;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        String sql = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());
        log.info("Добавлен пользователь с Id: " + user.getId() + " и именем: " + user.getName());
        return user;
    }

    @Override
    public User update(User user) {

        try {
            get(user.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Не был найден юзер с Id = " + user.getId());
        }

        String sqlUpdate = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlUpdate, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Обновлен пользователь с Id: " + user.getId() + " и названием: " + user.getName());

        return user;

    }

    @Override
    public void deleteById(int id) {
        User user = get(id);

        if (user.getId() != null) {
            String sql = "DELETE FROM USERS WHERE USER_ID = ?";
            jdbcTemplate.update(sql, id);
            log.info("Удален юзер с Id: " + user.getId());
        } else {
            throw new NotFoundException("Не найден юзер с id: " + id);
        }
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT USER_ID, EMAIL, USER_NAME, LOGIN, BIRTHDAY FROM USERS";
        try {
            return new ArrayList<>(jdbcTemplate.query(sql, this::mapRowToUser));
        } catch (Exception e) {
            throw new NotFoundException("No Users were found while running getAll()");
        }
    }

    @Override
    public User get(int id) {

        String sql = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS WHERE USER_ID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Не был найден юзер с Id = " + id);
        }

    }

    @Override
    public void addFriend(int id, int friendId) {

        User user = get(id);
        User friend = get(friendId);

        if (user.getId() != null && friend.getId() != null) {
            String sql = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?,?)";
            jdbcTemplate.update(sql, user.getId(), friend.getId());
        } else if (user.getId() == null) {
            throw new NotFoundException("Не найден юзер с id: " + id);
        } else if (friend.getId() == null) {
            throw new NotFoundException("Не найден друг с id: " + friendId);
        } else {
            throw new NotFoundException("Не найдены ни друг с id: " + friendId + ", ни юзер с id: " + id);
        }
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        User user = get(id);
        User friend = get(friendId);

        if (user.getId() != null && friend.getId() != null) {
            String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sql, user.getId(), friend.getId());
        } else if (user.getId() == null) {
            throw new NotFoundException("Не найден юзер с id: " + id);
        } else if (friend.getId() == null) {
            throw new NotFoundException("Не найден друг с id: " + id);
        } else {
            throw new NotFoundException("Не найдены ни друг с id: " + id + ", ни юзер с id: " + id);
        }
    }

    @Override
    public List<User> getFriends(int id) {

        String sql = "SELECT u.* FROM USERS u JOIN FRIENDS f ON u.USER_ID = f.FRIEND_ID WHERE f.USER_ID = ?";
        return new ArrayList<>(jdbcTemplate.query(sql, this::mapRowToUser, id));
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {

        String sql = "SELECT u.* FROM USERS u \n" +
                "JOIN FRIENDS f ON u.USER_ID = f.FRIEND_ID\n" +
                "JOIN FRIENDS f2 ON u.USER_ID = f2.FRIEND_ID \n" +
                "WHERE f.USER_ID = ? AND f2.USER_ID = ?";

        return new ArrayList<>(jdbcTemplate.query(sql, this::mapRowToUser, id, otherId));
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User
                .builder()
                .id(rs.getInt("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("USER_NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}