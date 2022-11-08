package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendsService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final FriendsService friendsService;

    @Autowired
    public UserController(UserService userService, FriendsService friendsService) {
        this.userService = userService;
        this.friendsService = friendsService;
    }

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
    private void validationUser (User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id){
        return userService.findUserById(id);
    }

    @GetMapping
    public Collection<User> findAllUsers() { return userService.findAllUsers(); }

    // GET /users/{id}/friends - возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> getSetOfFriends(@PathVariable Long id) {
        List<Friends> friends = friendsService.getSetOfFriends(id);
        return friends.stream()
                .map(friend -> findUserById(friend.getFriendId()))
                .collect(Collectors.toList());
    }

    // GET /users/{id}/friends/common/{otherId} - список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getSetOfCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return friendsService.getSetOfCommonFriends(id, otherId);
    }

    // PUT /users/{id}/friends/{friendId} - добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendToSetOfFriends(@PathVariable Long id, @PathVariable Long friendId) {
        friendsService.addFriendToSetOfFriends(id, friendId);
    }

    // DELETE /users/{id}/friends/{friendId} - удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendFromSetOfFriends(@PathVariable Long id, @PathVariable Long friendId) {
        friendsService.deleteFriendFromSetOfFriends(id, friendId);
    }

}
