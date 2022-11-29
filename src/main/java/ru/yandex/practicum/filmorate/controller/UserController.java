package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventFeedService;
import ru.yandex.practicum.filmorate.service.FriendsService;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final FriendsService friendsService;
    private final LikeService likeService;
    private final EventFeedService eventFeedService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Обрабатываем запрос на создание пользователя " + user);
        validationUser(user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Обрабатываем запрос на обновление пользователя " + user);
        validationUser(user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        log.info("Обрабатываем запрос на получение пользователя с id = " + id);
        return userService.findUserById(id);
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Обрабатываем запрос на получение списка всех пользователей");
        return userService.findAllUsers();
    }

    // GET /users/{id}/friends - возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> getSetOfFriends(@PathVariable Long id) {
        log.info("Обрабатываем запрос на получение списка друзей пользователя с id = " + id);
        return friendsService.getSetOfFriends(id);
    }

    // GET /users/{id}/friends/common/{otherId} - список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getSetOfCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Обрабатываем запрос на получение списка общих друзей пользователей с id = {} и id = {}",
                id, otherId);
        return friendsService.getSetOfCommonFriends(id, otherId);
    }

    // GET /users/{id}/feed - просмотр последних событий на платформе
    @GetMapping("/{id}/feed")
    public List<Event> getAllEventFeed(@PathVariable Long id) {
        log.info("Обрабатываем запрос на получение ленты событий пользователя с id = " + id);
        return eventFeedService.getAllEventFeed(id);
    }

    @GetMapping("/{userId}/recommendations")
    public List<Film> getRecommendations(@PathVariable Long userId) {
        log.info("Обрабатываем запрос на получение рекомендаций для пользователя с id = " + userId);
        return likeService.getRecommendations(userId);
    }

    // PUT /users/{id}/friends/{friendId} - добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendToSetOfFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Обрабатываем запрос на создание дружбы для пользователей с id = {} и id = {}", id, friendId);
        friendsService.addFriendToSetOfFriends(id, friendId);
    }

    // DELETE /users/{id}/friends/{friendId} - удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendFromSetOfFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Обрабатываем запрос на удаление дружбы для пользователей с id = {} и id = {}", id, friendId);
        friendsService.deleteFriendFromSetOfFriends(id, friendId);
    }

    @DeleteMapping("/{userId}")
    boolean deleteUser(@PathVariable long userId) {
        log.info("Обрабатываем запрос на удаление пользователя с id = " + userId);
        return userService.deleteUser(userId);
    }

    private void validationUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
