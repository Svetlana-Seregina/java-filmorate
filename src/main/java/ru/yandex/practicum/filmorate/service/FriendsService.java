package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendsService {

    private final FriendsDao friendsDao;
    private final UserService userService;
    private final EventFeedService eventFeedService;


    public List<User> getSetOfFriends(Long userId){
//      Добавил метод получения пользователя. Если пользователя нет в базе, то метод бросит исключение
//      и ендпоинт вернет нужный для тестов статус ошибки
        log.info("F7-1. Получение списка друзей пользователя");
        userService.findUserById(userId);
        return friendsDao.getSetOfFriends(userId);
    }

    public List<User> getSetOfCommonFriends(Long userId, Long otherId){
        log.info("F7-2. Получение списка общих друзей для двух пользователей");
        return friendsDao.getSetOfCommonFriends(userId, otherId);
    }

    public void addFriendToSetOfFriends(Long userId, Long friendId){
        log.info("F7-3. Создание дружбы для двух пользователей");
        if (friendsDao.addFriendToSetOfFriends(userId, friendId)) {
            eventFeedService.addFriendEvent(userId, friendId);
        }
    }

    public void deleteFriendFromSetOfFriends(Long userId, Long friendId){
        log.info("F7-4. Удаление дружбы для двух пользователей");
        if (friendsDao.deleteFriendFromSetOfFriends(userId, friendId)) {
            eventFeedService.removeFriendEvent(userId, friendId);
        }
    }
}
