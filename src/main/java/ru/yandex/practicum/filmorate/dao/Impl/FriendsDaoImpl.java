package ru.yandex.practicum.filmorate.dao.Impl;

import org.springframework.dao.DataIntegrityViolationException;
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
    public boolean addFriendToSetOfFriends(Long userId, Long friendId) {
        try {
            String sqlQuery = "INSERT into FRIEND (USER_ID, FRIEND_ID) values (?,?)";
            return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException("id пользователя не найдено в базе.");
        }
    }

    @Override
    public boolean deleteFriendFromSetOfFriends(Long userId, Long friendId){
        String sqlQuery = "delete from FRIEND where USER_ID = ? and FRIEND_ID = ?";
        return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
    }

}
