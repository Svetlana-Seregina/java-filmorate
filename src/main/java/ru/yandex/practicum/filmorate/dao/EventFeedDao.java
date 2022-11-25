package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventFeedDao {
    List<Event> getAllEventFeed(Long id);

    void addLikeEvent(Long userId, Long entity_id);

    void removeLikeEvent(Long userId, Long entity_id);

    void addFriendEvent(Long userId, Long entity_id);

    void removeFriendEvent(Long userId, Long entity_id);

    void addReviewEvent(Long userId, Long entity_id);

    void removeReviewEvent(Long userId, Long entity_id);

    void updateReviewEvent(Long userId, Long entity_id);
}
