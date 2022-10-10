package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;
    public Long getNextId() {
        return nextId++;
    }
    @Override
    public Map<Long, User> getUsers() {
        return users;
    }
    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        validationUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            validationUser(user);
            users.put(user.getId(), user);
        } else {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new UserNotFoundException("Пользователь не найден.");
        }
    }

    private void validationUser (User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователя с id " + id + "не существует");
        }
        return users.get(id);
    }

    @Override
    public List<User> getSetOfCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователя с id " + id + " не существует");
        }
        if (!users.containsKey(otherId)) {
            throw new UserNotFoundException("Пользователя с id " + otherId + " не существует");
        }

        Set<Long> commonSetOfFriend = users.get(id).getFriends().stream()
                    .filter(users.get(otherId).getFriends()::contains)
                    .collect(Collectors.toSet());

        for (Long commonFriend: commonSetOfFriend) {
            commonFriends.add(getUserById(commonFriend));
        }
        return commonFriends;
    }

}
