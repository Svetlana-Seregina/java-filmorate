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
    private final EventFeedService eventFeedService;


    public List<User> getSetOfFriends(Long userId){
//      Добавил метод получения пользователя. Если пользователя нет в базе, то метод бросит исключение
//      и ендпоинт вернет нужный для тестов статус ошибки
        userService.findUserById(userId);
        return friendsDao.getSetOfFriends(userId);
    }

    public List<User> getSetOfCommonFriends(Long userId, Long otherId){
        return friendsDao.getSetOfCommonFriends(userId, otherId);
    }

    public void addFriendToSetOfFriends(Long userId, Long friendId){
        if (friendsDao.addFriendToSetOfFriends(userId, friendId)) {
            eventFeedService.addFriendEvent(userId, friendId);
        }
    }

    public void deleteFriendFromSetOfFriends(Long userId, Long friendId){
        if (friendsDao.deleteFriendFromSetOfFriends(userId, friendId)) {
            eventFeedService.removeFriendEvent(userId, friendId);
        }
    }
}
