package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.*;

@Service
public class UserService {
    private final UserDao userStorage;
    public UserService(UserDao userStorage) {
        this.userStorage = userStorage;
    }
    public User createUser(User user) {
        return userStorage.createUser(user);
    }
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }
    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }

}
