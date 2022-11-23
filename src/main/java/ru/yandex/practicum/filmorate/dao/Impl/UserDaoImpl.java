package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

@Repository
@Slf4j
public class UserDaoImpl implements ru.yandex.practicum.filmorate.dao.UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into USERS(NAME, EMAIL, LOGIN, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update USERS set NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? where USER_ID = ?";
        int updatedRows = jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Пользователь не найден, user id = " + user.getId());
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) {
        try {
            String sqlUserRow = "select * from USERS where USER_ID = ?";
            return jdbcTemplate.queryForObject(sqlUserRow, UserDaoImpl::mapRowToUser, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Пользователь с user_id=%d не найден", userId));
        }
    }

    @Override
    public Collection<User> findAllUsers() {
        String sqlQuery = "select USER_ID, NAME, EMAIL, LOGIN, BIRTHDAY from USERS";
        return jdbcTemplate.query(sqlQuery, UserDaoImpl::mapRowToUser);
    }

    @Override
    public boolean deleteUser(long userId) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sqlQuery, userId) > 0;
    }

    public static User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("NAME"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
