package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendsService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FriendsService friendsService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validationUser(user);
        return userService.createUser(user);
    }
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validationUser(user);
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable Long userId){
        return userService.findUserById(userId);
    }

    @GetMapping
    public Collection<User> findAllUsers() { return userService.findAllUsers(); }

    // GET /users/{id}/friends - возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{userId}/friends")
    public List<User> getSetOfFriends(@PathVariable Long userId) {
        return friendsService.getSetOfFriends(userId);
    }


    // GET /users/{id}/friends/common/{otherId} - список друзей, общих с другим пользователем
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getSetOfCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        return friendsService.getSetOfCommonFriends(userId, otherId);
    }

    // PUT /users/{id}/friends/{friendId} - добавление в друзья
    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriendToSetOfFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        friendsService.addFriendToSetOfFriends(userId, friendId);
    }

    // DELETE /users/{id}/friends/{friendId} - удаление из друзей
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriendFromSetOfFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        friendsService.deleteFriendFromSetOfFriends(userId, friendId);
    }

    private void validationUser (User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
