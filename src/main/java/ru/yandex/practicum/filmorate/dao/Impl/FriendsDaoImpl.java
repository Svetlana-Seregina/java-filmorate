package ru.yandex.practicum.filmorate.dao.Impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbcTemplate;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getSetOfFriends(Long userId) {
       String sqlQuery = "SELECT * FROM USERS u, FRIEND f where u.USER_ID = f.FRIEND_ID AND f.USER_ID = ?";
        List<User> listOfFriends = jdbcTemplate.query(sqlQuery, UserDaoImpl::mapRowToUser, userId);
        return listOfFriends;
    }

    @Override
    public List<User> getSetOfCommonFriends(Long userId, Long otherId) {
        String sqlQuery = "SELECT * FROM USERS u, FRIEND f, FRIEND o " +
                "where u.USER_ID = f.FRIEND_ID AND u.USER_ID = o.FRIEND_ID AND f.USER_ID = ? AND o.USER_ID = ?";

          List<User> setOfFriends = jdbcTemplate.query(sqlQuery, UserDaoImpl::mapRowToUser, userId, otherId);
        return setOfFriends;
    }

    @Override
    public void addFriendToSetOfFriends(Long userId, Long friendId) {
        String sqlQuery = "INSERT into FRIEND (USER_ID, FRIEND_ID) values (?,?)";
        if (friendId <= 0) {
            throw new EntityNotFoundException("friend_id не может быть меньше нуля" + friendId);
        }
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriendFromSetOfFriends(Long userId, Long friendId){
        String sqlQuery = "delete from FRIEND where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

}
