package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage, FriendsDao {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;
    public Long getNextId() {
        return nextId++;
    }

    public Map<Long, User> getUsers() {
        return users;
    }
    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
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
            throw new EntityNotFoundException("Пользователь не найден.");
        }
        return user;
    }


    public void deleteUser(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new EntityNotFoundException("Пользователь не найден.");
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
    public User findUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        return users.get(id);
    }


    public List<User> getSetOfCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        if (!users.containsKey(otherId)) {
            throw new EntityNotFoundException("Пользователя с id " + otherId + " не существует");
        }

        Set<Long> commonSetOfFriend = users.get(id).getFriends().stream()
                .filter(users.get(otherId).getFriends()::contains)
                .collect(Collectors.toSet());

        for (Long commonFriend: commonSetOfFriend) {
            commonFriends.add(findUserById(commonFriend));
        }
        return commonFriends;
    }

    @Override
    public void addFriendToSetOfFriends(Long userId, Long friendId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException("Пользователя с id " + userId + " не существует");
        }
        if (!users.containsKey(friendId)) {
            throw new EntityNotFoundException("Пользователя с id " + friendId + " не существует");
        }

        if(users.get(userId).getFriends().contains(friendId)) {
            throw new ValidationException("Пользователь с таким id " + userId + " уже есть в списке друзей.");
        }
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
    }

    @Override
    public void deleteFriendFromSetOfFriends(Long id, Long friendId) {
        if (users.containsKey(id) && users.containsKey(friendId)) {
            users.get(id).getFriends().remove(friendId);
            users.get(friendId).getFriends().remove(id);
        } else {
            throw new EntityNotFoundException("Пользователь с таким id" + id + " отсутствует в списке друзей");
        }
    }

    @Override
    public List<Friends> getSetOfFriends(Long id) {
        /*List<User> friends = new ArrayList<>();
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException("Пользователя с таким id " + id + " не существует.");
        }
        if (users.get(id).getFriends().size() == 0) {
            throw new ValidationException("Друзей в списке нет.");
        }
        Set<Long> friendsSet = users.get(id).getFriends();

        for (Long friend: friendsSet) {
            friends.add(findUserById(friend));
        }
        return friends;*/
        return Collections.emptyList();
    }

}