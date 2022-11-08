package ru.yandex.practicum.filmorate.dao.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

@Component
@Qualifier("UserDbStorage")
@Repository
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
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
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());

        return findUserById(user.getId());
    }


    @Override
    public User findUserById(Long id) {
        try {
            String sqlUserRow = "select * from USERS where USER_ID = ?";
            return jdbcTemplate.queryForObject(sqlUserRow, UserDbStorage::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Пользователь с user_id=%d не найден", id));
        }
    }

    @Override
    public Collection<User> findAllUsers() {
        String sqlQuery = "select USER_ID, NAME, EMAIL, LOGIN, BIRTHDAY from USERS";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::mapRowToUser);
    }

    public static User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        // This method is an implementation of the functional interface RowMapper.
        // It is used to map each row of a ResultSet to an object.
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("NAME"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }



}
