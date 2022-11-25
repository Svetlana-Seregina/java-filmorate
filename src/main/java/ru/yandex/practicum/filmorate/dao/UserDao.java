package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserDao {

    User findUserById(Long userId);
    User createUser(User user);
    User updateUser(User user);
    Collection<User> findAllUsers();
    boolean deleteUser(Long userId);

}
