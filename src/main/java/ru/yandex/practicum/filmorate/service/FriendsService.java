package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.FriendsDaoImpl;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service

public class FriendsService {

    private final FriendsDaoImpl friendDaoImpl;

    public FriendsService(FriendsDaoImpl friendDaoImpl) {
        this.friendDaoImpl = friendDaoImpl;
    }

    public List<Friends> getSetOfFriends(Long id){
        return friendDaoImpl.getSetOfFriends(id);
    }
    public List<User> getSetOfCommonFriends(Long id, Long otherId){
        return friendDaoImpl.getSetOfCommonFriends(id, otherId);
    }

    public void addFriendToSetOfFriends(Long userId, Long friendId){
        friendDaoImpl.addFriendToSetOfFriends(userId, friendId);
    }

    public void deleteFriendFromSetOfFriends(Long id, Long friendId){
        friendDaoImpl.deleteFriendFromSetOfFriends(id, friendId);
    }
}
