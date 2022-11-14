package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {

    List<User> getSetOfFriends(Long userId);
    List<User> getSetOfCommonFriends(Long userId, Long otherId);
    void addFriendToSetOfFriends(Long userId, Long friendId);
    void deleteFriendFromSetOfFriends(Long userId, Long friendId);

}
