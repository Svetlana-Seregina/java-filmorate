package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserDao {
    // хранения, обновления и поиска объектов

    User findUserById(Long id);
    User createUser(User user);
    User updateUser(User user);
    Collection<User> findAllUsers();


}
