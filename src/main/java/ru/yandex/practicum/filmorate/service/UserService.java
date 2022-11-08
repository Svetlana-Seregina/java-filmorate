package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class UserService {
    private final UserDbStorage userDbStorage;
    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }
    public User createUser(User user) {
        return userDbStorage.createUser(user);
    }
    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }
    public Collection<User> findAllUsers() {
        return userDbStorage.findAllUsers();
    }
    public User findUserById(Long id) {
        return userDbStorage.findUserById(id);
    }

}
