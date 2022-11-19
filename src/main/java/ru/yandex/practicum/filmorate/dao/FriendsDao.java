package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {

    List<User> getSetOfFriends(Long userId);
    List<User> getSetOfCommonFriends(Long userId, Long otherId);
    boolean addFriendToSetOfFriends(Long userId, Long friendId);
    boolean deleteFriendFromSetOfFriends(Long userId, Long friendId);

}
