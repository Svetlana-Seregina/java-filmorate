package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {

    List<Friends> getSetOfFriends(Long id);
    List<User> getSetOfCommonFriends(Long id, Long otherId);
    void addFriendToSetOfFriends(Long userId, Long friendId);
    void deleteFriendFromSetOfFriends(Long id, Long friendId);

}
