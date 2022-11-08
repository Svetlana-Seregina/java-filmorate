package ru.yandex.practicum.filmorate.dao.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Qualifier("friendsDaoImpl")
@Repository
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbcTemplate;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Friends> getSetOfFriends(Long id) {
        String sqlQuery = "SELECT FRIEND_ID FROM FRIEND WHERE USER_ID = ?";
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Friends(id, rs.getLong("friend_id")),
                id);
    }

    @Override
    public List<User> getSetOfCommonFriends(Long id, Long otherId) {
        String sqlQuery = "SELECT * from USERS"+
                " WHERE user_id in (SELECT FRIEND_ID FROM FRIEND WHERE user_id = ?) AND " +
                " user_id IN (SELECT FRIEND_ID FROM FRIEND WHERE user_id = ?)";
          List<User> setOfFriends = jdbcTemplate.query(sqlQuery, UserDbStorage::mapRowToUser, id, otherId);
        return setOfFriends;
    }

    @Override
    public void addFriendToSetOfFriends(Long userId, Long friendId) {
        String sqlQuery = String.format("INSERT into FRIEND(USER_ID, FRIEND_ID) values (%d, %d)", userId, friendId);
        if (friendId <= 0) {
            throw new EntityNotFoundException("friend_id не может быть меньше нуля" + friendId);
        }
        jdbcTemplate.execute(sqlQuery);
    }

    @Override
    public void deleteFriendFromSetOfFriends(Long id, Long friendId){
        String sqlQuery = String.format("DELETE from FRIEND where USER_ID = %d AND FRIEND_ID = %d", id, friendId);
        jdbcTemplate.execute(sqlQuery);

    }
}
