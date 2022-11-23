package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsDao friendsDao;
    private final UserService userService;
    public List<User> getSetOfFriends(Long userId){
        userService.findUserById(userId);
        return friendsDao.getSetOfFriends(userId);
    }

    public List<User> getSetOfCommonFriends(Long userId, Long otherId){
        return friendsDao.getSetOfCommonFriends(userId, otherId);
    }

    public void addFriendToSetOfFriends(Long userId, Long friendId){
        friendsDao.addFriendToSetOfFriends(userId, friendId);
    }

    public void deleteFriendFromSetOfFriends(Long userId, Long friendId){
        friendsDao.deleteFriendFromSetOfFriends(userId, friendId);
    }

}
