package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
//import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
   /* private final UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    void findAll() {
        userController.create(User.builder()
                .name("Maria0")
                .email("maria0@yandex.ru")
                .login("masha235")
                .birthday(LocalDate.of(2000, 11, 15))
                .build());
        assertEquals(1, userController.findAll().size());
    }

    @Test
    void findUser() {
        User user = User.builder()
                .name("Maria")
                .email("maria0@yandex.ru")
                .login("masha235")
                .birthday(LocalDate.of(2000, 11, 15))
                .build();
        userController.create(user);
        assertEquals(1, userController.findUser(1L).getId());
    }

    @Test
    void createAndDelete() {
        User user = User.builder()
                .name("Maria")
                .email("maria0@yandex.ru")
                .login("masha235")
                .birthday(LocalDate.of(2000, 11, 15))
                .build();
        assertEquals("Maria", userController.create(user).getName());
        userController.deleteUser(1L);
        assertThrows(EntityNotFoundException.class, () -> userController.findUser(1L));
    }


   @Test
    void addFriendToSetOfFriends() {
       User user1 = User.builder()
               .name("Maria1")
               .email("maria1@yandex.ru")
               .login("masha2351")
               .birthday(LocalDate.of(2000, 11, 15))
               .build();
       userController.create(user1);
       User user2 = User.builder()
               .name("Maria2")
               .email("maria2@yandex.ru")
               .login("masha2352")
               .birthday(LocalDate.of(2000, 12, 15))
               .build();
        userController.create(user2);
        userController.addFriendToSetOfFriends(1L,2L);
        assertEquals(1, user1.getFriends().size());
        assertEquals(1, userController.getSetOfFriends(1L).size());
        userController.deleteFriendFromSetOfFriends(1L, 2L);
        assertEquals(0, userController.getSetOfCommonFriends(1L, 2L).size());
        assertThrows(ValidationException.class,
               () -> userController.getSetOfFriends(1L));

    }

    @Test
    void getSetOfCommonFriends() {
        assertEquals(0, userController.create(User.builder()
                .name("Maria")
                .email("maria@yandex.ru")
                .login("masha235")
                .birthday(LocalDate.of(2000, 11, 15))
                .build()).getFriends().size());
    }

    @Test
    void updateUser() {
        userController.create(User.builder()
                .name("Maria")
                .email("maria@yandex.ru")
                .login("masha235")
                .birthday(LocalDate.of(2000, 11, 15))
                .build());
        assertEquals("mariamaria", userController.updateUser(User.builder()
                .id(1L)
                .name("Maria")
                .email("maria@yandex.ru")
                .login("mariamaria")
                .birthday(LocalDate.of(2000, 11, 15))
                .build()).getLogin());
    }


   @Test
    void createUserWithoutOrIncorrectLogin() {
        assertThrows(ValidationException.class,
                () -> userController.create(User.builder()
                        .name("Maria")
                        .email("maria@yandex.ru")
                        .login(" ")
                        .birthday(LocalDate.of(2000, 11, 15))
                        .build()));
    }

  @Test
    void createUserWithEmptyName() {
        assertEquals("masha235", userController.create(User.builder()
                .name("")
                .email("maria0@yandex.ru")
                .login("masha235")
                .birthday(LocalDate.of(2000, 11, 15))
                .build()).getName());
    }

   @Test
    void createUserWithoutName() {
        assertEquals("masha235", userController.create(User.builder()
                .name(null)
                .email("maria0@yandex.ru")
                .login("masha235")
                .birthday(LocalDate.of(2000, 11, 15))
                .build()).getName());
    }*/

}
