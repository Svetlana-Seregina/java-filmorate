package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public Collection<User> findAll() { return userService.findAll(); }
    @GetMapping("/{id}")
    public User findUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    @GetMapping("/{id}/friends")
    public List<User> getSetOfFriends(@PathVariable Long id) {
        return userService.getSetOfFriends(id);
    }
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getSetOfCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getSetOfCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
            return userService.create(user);
    }


    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendToSetOfFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriendToSetOfFriends(id, friendId);
    }


    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendFromSetOfFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriendFromSetOfFriends(id, friendId);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
