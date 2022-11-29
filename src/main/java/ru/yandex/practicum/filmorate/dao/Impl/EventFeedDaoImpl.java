package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.EventFeedDao;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventFeedDaoImpl implements EventFeedDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getAllEventFeed(Long id) {
        String getAllEventFeed = "SELECT * FROM feed WHERE user_id = ?";
        return jdbcTemplate.query(getAllEventFeed, this::mapRowToEvent, id);
    }

    @Override
    public void addLikeEvent(Long userId, Long entity_id) {
        insertEvent(userId, entity_id, "LIKE", "ADD");
    }

    @Override
    public void removeLikeEvent(Long userId, Long entity_id) {
        insertEvent(userId, entity_id, "LIKE", "REMOVE");
    }

    @Override
    public void addFriendEvent(Long userId, Long entity_id) {
        insertEvent(userId, entity_id, "FRIEND", "ADD");
    }

    @Override
    public void removeFriendEvent(Long userId, Long entity_id) {
        insertEvent(userId, entity_id, "FRIEND", "REMOVE");
    }

    @Override
    public void addReviewEvent(Long userId, Long entity_id) {
        insertEvent(userId, entity_id, "REVIEW", "ADD");
    }

    @Override
    public void removeReviewEvent(Long userId, Long entity_id) {
        insertEvent(userId, entity_id, "REVIEW", "REMOVE");
    }

    @Override
    public void updateReviewEvent(Long userId, Long entity_id) {
        insertEvent(userId, entity_id, "REVIEW", "UPDATE");
    }

    private void insertEvent(Long userId, Long entity_id, String eventType, String operation){
        String insertEvent = "INSERT INTO feed (user_id, entity_id, event_type, operation, " +
                "timestamp) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertEvent, userId, entity_id, eventType, operation,
                Timestamp.valueOf(LocalDateTime.now()));
    }

    private Event mapRowToEvent(ResultSet resultSet, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(resultSet.getLong("event_id"))
                .userId(resultSet.getLong("user_id"))
                .entityId(resultSet.getLong("entity_id"))
                .eventType(resultSet.getString("event_type"))
                .operation(resultSet.getString("operation"))
                .timestamp(resultSet.getTimestamp("timestamp").getTime())
                .build();
    }
}
