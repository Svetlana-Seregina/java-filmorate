package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventFeedDao;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventFeedService {
    private final EventFeedDao eventFeedDao;

    public List<Event> getAllEventFeed(Long id) {
        log.info("F1-1. Получение всех событий пользователя");
        return eventFeedDao.getAllEventFeed(id);
    }

    public void addLikeEvent(Long userId, Long entity_id) {
        log.info("F1-2. Добавление события лайк");
        eventFeedDao.addLikeEvent(userId, entity_id);
    }

    public void removeLikeEvent(Long userId, Long entity_id) {
        log.info("F1-3. Удаление события лайк");
        eventFeedDao.removeLikeEvent(userId, entity_id);
    }

    public void addFriendEvent(Long userId, Long entity_id) {
        log.info("F1-4. Добавление события дружба");
        eventFeedDao.addFriendEvent(userId, entity_id);
    }

    public void removeFriendEvent(Long userId, Long entity_id) {
        log.info("F1-5. Удаление события дружба");
        eventFeedDao.removeFriendEvent(userId, entity_id);
    }

    public void addReviewEvent(Long userId, Long entity_id) {
        log.info("F1-6. Добавление события отзыв");
        eventFeedDao.addReviewEvent(userId, entity_id);
    }

    public void removeReviewEvent(Long userId, Long entity_id) {
        log.info("F1-7. Удаление события отзыв");
        eventFeedDao.removeReviewEvent(userId, entity_id);
    }

    public void updateReviewEvent(Long userId, Long entity_id) {
        log.info("F1-8. Обновление события отзыв");
        eventFeedDao.updateReviewEvent(userId, entity_id);
    }
}
