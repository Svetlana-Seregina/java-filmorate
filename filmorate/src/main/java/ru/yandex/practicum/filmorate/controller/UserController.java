package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
public class UserController {

    private final Set<User> users = new HashSet<>();

    @GetMapping("/users")
    public Set<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Email пользователя: {}, Логин: {}, Имя пользователя: {}, Дата рождения: {}",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() == null
                || user.getLogin().isBlank()
                || !user.getLogin().equals(user.getLogin().replaceAll("\\s",""))) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getName().isBlank()) {
                user.setName(user.getLogin());
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        users.add(user);
        return user;
    }

    @PutMapping("/users")
    public User createAndUpdateUser(@RequestBody User user) {
        if (users.contains(user)) {
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new ValidationException("Электронная почка не может быть пустой");
            } else
                users.add(user);
        }
        return user;
    }


}
