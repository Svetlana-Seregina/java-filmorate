package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;
    public Integer getNextId() {
        return nextId++;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
            validationUser(user);
            user.setId(getNextId());
            users.put(user.getId(), user);
            if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            log.info("Email пользователя: {}, Логин: {}, Имя пользователя: {}, Дата рождения: {}",
                    user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
            return user;
    }


    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            validationUser(user);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("такой id отсутствует");
        }
            log.info("Email пользователя: {}, Логин: {}, Имя пользователя: {}, Дата рождения: {}",
                    user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
            return user;
    }


    private void validationUser (User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать символ @");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

}
