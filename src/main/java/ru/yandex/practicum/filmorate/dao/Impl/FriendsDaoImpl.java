package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getSetOfFriends(Long userId) {
            String sqlQuery = "SELECT * FROM users u, friend f WHERE u.user_id = f.friend_id AND f.user_id = ?";
            List<User> listOfFriends = jdbcTemplate.query(sqlQuery, UserDaoImpl::mapRowToUser, userId);
            return listOfFriends;
    }

    @Override
    public List<User> getSetOfCommonFriends(Long userId, Long otherId) {
        String sqlQuery = "SELECT * FROM users u, friend f, friend o " +
                "WHERE u.user_id = f.friend_id AND u.user_id = o.friend_id AND f.user_id = ? AND o.user_id = ?";
        List<User> setOfFriends = jdbcTemplate.query(sqlQuery, UserDaoImpl::mapRowToUser, userId, otherId);
        return setOfFriends;
    }

    @Override
    public boolean addFriendToSetOfFriends(Long userId, Long friendId) {
        try {
            String sqlQuery = "INSERT INTO friend (user_id, friend_id) VALUES (?,?)";
            return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException("id пользователя не найдено в базе.");
        }
    }

    @Override
    public boolean deleteFriendFromSetOfFriends(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM friend WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
    }
}
