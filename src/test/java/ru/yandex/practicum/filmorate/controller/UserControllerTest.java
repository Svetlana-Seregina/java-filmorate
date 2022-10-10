package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    //private final UserController userController = new UserController();

    /*@Test
    void getSetOfCommonFriends() {
        User user = new User(0, "Maria","maria@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15), null);
        userController.create(user);
        assertEquals(user, userController.create(user));
        assertNull(null);
    }*/
   /* @Test
    void addFriendToSetOfFriends() {
        User user1 = new User(0L, "Maria0","maria0@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15), null);
        userController.create(user1);
        User user2 = new User(1L, "Maria1","maria1@yandex.ru", "masha235",
                LocalDate.of(2000, 12, 15), null);
        userController.create(user2);
        userController.addFriendToSetOfFriends(0L,1L);
        assertEquals(1, user1.getFriends().size());

    }*/
   /*@Test
    void findAll() {
        assertEquals(0, userController.findAll().size());
        User user = new User(0, "Maria","maria@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15));
        userController.create(user);
        assertEquals(1, userController.findAll().size());
    }*/

   /*@Test
    void create() {
        User user = new User(0L, "Maria", "maria@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15), null);
        userController.create(user);
        assertEquals("Maria", userController.create(user).getName());
    }*/

    /*@Test
    void createUserWithoutOrIncorrectLogin() {
        User user1 = new User(0, "Maria","maria@yandex.ru", "masha 235",
                LocalDate.of(2000, 11, 15));
        assertThrows(ValidationException.class,
                () -> userController.create(user1));
    }*/

   /* @Test
    void createUserWithEmptyName() {
        User user = new User(0, null, "maria@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15));
        assertEquals("masha235", userController.create(user).getName());
    }*/

   /* @Test
    void createUserWithoutName() {
        User user = new User(0, "","maria@yandex.ru", "masha235",
                LocalDate.of(2000, 11, 15));
        assertEquals("masha235", userController.create(user).getName());
    }*/

}