package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    // хранения, обновления и поиска объектов
    User create(User user);
    void deleteUser(Long id);
    User updateUser(User user);
    Map<Long, User> getUsers();
    Collection<User> findAll();
    User getUserById(Long id);
    List<User> getSetOfCommonFriends(Long id, Long otherId);

}
