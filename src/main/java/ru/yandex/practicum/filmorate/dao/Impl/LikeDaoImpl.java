package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collections;
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
    public List<Film> findPopularFilms(Integer count) {
        String sqlQuery = "SELECT *, mpa.NAME AS mpa_name " +
                " FROM FILMS AS f LEFT JOIN " +
                " (SELECT FILM_ID, COUNT(FILM_ID) AS likes_count " +
                "  FROM LIKES  GROUP BY FILM_ID) AS likes_by_film ON likes_by_film.FILM_ID = f.FILM_ID " +
                " INNER JOIN mpa ON mpa.MPA_ID = f.MPA_ID " +
                " ORDER BY likes_by_film.likes_count DESC " +
                " LIMIT " + count;

        List<Film> listOfFilms = jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm);
        return listOfFilms;
    }

    @Override
    public List<Long> getRecommendations(Long firstUserId) {
        String sqlQuery = "SELECT user_id, count(*) as cnt FROM likes WHERE film_id IN ( \n" +
                "SELECT film_id  FROM films WHERE film_id IN (\n" +
                "                SELECT film_id FROM likes WHERE user_id = ?))\n" +
                "                AND user_id != ?\n" +
                "GROUP BY user_id\n" +
                "ORDER BY cnt DESC\n" +
                "LIMIT 1";

        long secondUserId;
        try {
             secondUserId = jdbcTemplate.queryForObject(sqlQuery,(rs, rowNum) -> rs.getLong("user_id")
                    , firstUserId, firstUserId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.EMPTY_LIST;
        }

        sqlQuery = "SELECT FILM_ID  FROM FILMS WHERE FILM_ID IN (\n" +
                "SELECT FILM_ID  FROM LIKES WHERE user_id = ?\n" +
                "AND FILM_ID NOT IN (SELECT FILM_ID  FROM LIKES WHERE user_id = ?))";

        return jdbcTemplate.query(sqlQuery,(rs, rowNum) -> rs.getLong("film_id"), secondUserId, firstUserId);
    }
}
