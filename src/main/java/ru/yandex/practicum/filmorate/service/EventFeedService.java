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

    public List<Event> getAllEventFeed(Long id){
        return eventFeedDao.getAllEventFeed(id);
    }

    public void addLikeEvent(Long userId, Long entity_id) {
        eventFeedDao.addLikeEvent(userId, entity_id);
    }


    public void removeLikeEvent(Long userId, Long entity_id) {
        eventFeedDao.removeLikeEvent(userId, entity_id);
    }

    public void addFriendEvent(Long userId, Long entity_id) {
        eventFeedDao.addFriendEvent(userId, entity_id);
    }

    public void removeFriendEvent(Long userId, Long entity_id) {
        eventFeedDao.removeFriendEvent(userId, entity_id);
    }

    public void addReviewEvent(Long userId, Long entity_id) {
        eventFeedDao.addReviewEvent(userId, entity_id);
    }

    public void removeReviewEvent(Long userId, Long entity_id) {
        eventFeedDao.removeReviewEvent(userId, entity_id);
    }

    public void updateReviewEvent(Long userId, Long entity_id) {
        eventFeedDao.updateReviewEvent(userId, entity_id);
    }
}
