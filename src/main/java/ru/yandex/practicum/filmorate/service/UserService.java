package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public User createUser(User user) {
        return userDao.createUser(user);
    }

    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    public Collection<User> findAllUsers() {
        return userDao.findAllUsers();
    }

    public User findUserById(Long userId) {
        return userDao.findUserById(userId);
    }

    public boolean deleteUser(long userId) {
        return userDao.deleteUser(userId);
    }
}
