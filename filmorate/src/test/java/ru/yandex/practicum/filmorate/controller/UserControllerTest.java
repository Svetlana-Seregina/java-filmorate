package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {
    UserController userController = new UserController();

    @Test
    void findAll() {
        User user = new User(0, "maria@yandex.ru", "masha235", "Maria",
                LocalDate.of(2000, 11, 15));

        assertEquals(user, userController.create(user));
        assertEquals(1, userController.findAll().size());
    }

    @Test
    void createUserWithoutOrIncorrectEmail() {
        User user1 = new User(0, null, "masha235", "Maria",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user1));

        User user2 = new User(0, "mariayandex.ru", "masha235", "Maria",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user2));

        User user3 = new User(0, "", "masha235", "Maria",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user3));

    }

    @Test
    void createUserWithoutOrIncorrectLogin() {
        User user1 = new User(0, "maria@yandex.ru", "masha 235", "Maria",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user1));

        User user2 = new User(0, "maria@yandex.ru", "", "Maria",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user2));
    }

    @Test
    void createUserWithIncorrectBirthday() {
        User user = new User(0, "maria@yandex.ru", "masha235", "Maria",
                LocalDate.of(2030, 11, 15));

        assertThrows(ValidationException.class,
                () -> userController.create(user));
    }

}