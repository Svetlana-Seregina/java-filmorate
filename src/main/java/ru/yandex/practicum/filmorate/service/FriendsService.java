package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.Impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendsService {

    private final FriendsDao friendDao;
    private final UserDaoImpl userDbStorage;

    public FriendsService(FriendsDao friendDao, UserDaoImpl userDbStorage) {
        this.friendDao = friendDao;
        this.userDbStorage = userDbStorage;
    }

    public List<User> getSetOfFriends(Long id){
        List<Friends> friends = friendDao.getSetOfFriends(id);
        return friends.stream()
                .map(friend -> userDbStorage.findUserById(friend.getFriendId()))
                .collect(Collectors.toList());
    }

    public List<User> getSetOfCommonFriends(Long id, Long otherId){
        return friendDao.getSetOfCommonFriends(id, otherId);
    }

    public void addFriendToSetOfFriends(Long userId, Long friendId){
        friendDao.addFriendToSetOfFriends(userId, friendId);
    }

    public void deleteFriendFromSetOfFriends(Long id, Long friendId){
        friendDao.deleteFriendFromSetOfFriends(id, friendId);
    }
}
