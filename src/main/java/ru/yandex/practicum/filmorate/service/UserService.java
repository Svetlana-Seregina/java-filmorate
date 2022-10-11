package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Service
public class UserService {
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User getUserById(Long id) {
        return inMemoryUserStorage.getUserById(id);
    }

    public List<User> getSetOfCommonFriends(Long id, Long otherId) {
        return inMemoryUserStorage.getSetOfCommonFriends(id, otherId);
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    public void deleteUser(Long id) {
        inMemoryUserStorage.deleteUser(id);
    }
    public void addFriendToSetOfFriends(Long id, Long friendId) {

        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new EntityNotFoundException("Пользователя с id " + friendId + " не существует");
        }

            if(inMemoryUserStorage.getUsers().get(id).getFriends().contains(friendId)) {
                throw new ValidationException("Пользователь с таким id " + id + " уже есть в списке друзей.");
            }
            inMemoryUserStorage.getUsers().get(id).getFriends().add(friendId);
            inMemoryUserStorage.getUsers().get(friendId).getFriends().add(id);
    }

    public void deleteFriendFromSetOfFriends(Long id, Long FriendId) {
        if (inMemoryUserStorage.getUsers().containsKey(id) && inMemoryUserStorage.getUsers().containsKey(FriendId)) {
                inMemoryUserStorage.getUsers().get(id).getFriends().remove(FriendId);
                inMemoryUserStorage.getUsers().get(FriendId).getFriends().remove(id);
            } else {
                throw new EntityNotFoundException("Пользователь с таким id" + id + " отсутствует в списке друзей");
            }
        }


    public List<User> getSetOfFriends(Long id) {
        List<User> friends = new ArrayList<>();
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new EntityNotFoundException("Пользователя с таким id " + id + " не существует.");
        }
        if (inMemoryUserStorage.getUsers().get(id).getFriends().size() == 0) {
            throw new ValidationException("Друзей в списке нет.");
        }
        Set<Long> friendsSet = inMemoryUserStorage.getUsers().get(id).getFriends();

        for (Long friend: friendsSet) {
            friends.add(inMemoryUserStorage.getUserById(friend));
        }
        return friends;
    }
}
