package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public Collection<User> findAll() { return userService.findAll(); }
    @GetMapping("/users/{id}")
    public User findUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }
    @GetMapping("/users/{id}/friends")
    public List<User> getSetOfFriends(@PathVariable("id") Long id) {
        return userService.getSetOfFriends(id);
    }
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getSetOfCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getSetOfCommonFriends(id, otherId);
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
            return userService.create(user);
    }


    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriendToSetOfFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriendToSetOfFriends(id, friendId);
    }


    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriendFromSetOfFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriendFromSetOfFriends(id, friendId);
    }
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
