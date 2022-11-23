package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLikeToFilm(Long id, Long userId) {
        String sqlQuery = "INSERT INTO LIKES (USER_ID, FILM_ID) values (?, ?)";
        return jdbcTemplate.update(sqlQuery, userId, id) > 0;
    }

    @Override
    public boolean removeLikeFromFilm(Long filmId, Long userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public List<Film> findPopularFilms(Integer count, Long genreId, Date year) {
        return Collections.emptyList();
    }


}
