package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserDao userDao;

    public User createUser(User user) {
        log.info("F5-1. Создание пользователя");
        return userDao.createUser(user);
    }

    public User updateUser(User user) {
        log.info("F5-4. Обновление пользователя");
        return userDao.updateUser(user);
    }

    public Collection<User> findAllUsers() {
        log.info("F5-3. Получение всех пользователей");
        return userDao.findAllUsers();
    }

    public User findUserById(Long userId) {
        log.info("F5-2. Получение пользователя по id");
        return userDao.findUserById(userId);
    }

    public boolean deleteUser(Long userId) {
        log.info("F5-5. Удаление пользователя");
        return userDao.deleteUser(userId);
    }
}
