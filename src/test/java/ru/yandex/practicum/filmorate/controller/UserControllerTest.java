package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.constraints.Email;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {
    private UserController userController = new UserController();

    @Test
    void findAll() {
        assertEquals(0, userController.findAll().size());
        User user = new User(0, "Maria","maria@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15));
        userController.create(user);
        assertEquals(1, userController.findAll().size());
    }

   @Test
    void create() {
        User user = new User(0, "Maria", "maria@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15));
        userController.create(user);
        assertEquals(user, userController.create(user));
    }

    @Test
    void createUserWithoutOrIncorrectEmail() {
        User user1 = new User(0, "Maria", null, "masha235",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user1));

        User user2 = new User(0, "Maria", "mariayandex.ru", "masha235",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user2));

        User user3 = new User("", "masha235",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user3));
    }

    @Test
    void createUserWithoutOrIncorrectLogin() {
        User user1 = new User(0, "Maria","maria@yandex.ru", "masha 235",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user1));

        User user2 = new User(0, "Maria","maria@yandex.ru", "",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user2));
    }

    @Test
    void createUserWithEmptyName() {
        User user = new User(0, null, "maria@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15));
        assertEquals("masha235", userController.create(user).getName());

    }

    @Test
    void createUserWithoutName() {
        User user = new User(0, "","maria@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15));
        assertEquals("masha235", userController.create(user).getName());
    }

    @Test
    void createUserWithIncorrectBirthday() {
        User user = new User(0, "Maria","maria@yandex.ru", "masha235",
                LocalDate.of(2030, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user));
    }

}