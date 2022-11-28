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

    private static final String SELECT_FILM_BY_FILM_ID = "SELECT film_id FROM films " +
            "WHERE film_id IN (" +
            "SELECT film_id  FROM likes WHERE user_id = ?";

    @Override
    public boolean addLikeToFilm(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
        return jdbcTemplate.update(sqlQuery, userId, filmId) > 0;
    }

    @Override
    public boolean removeLikeFromFilm(Long filmId, Long userId) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }


    @Override
    public List<Film> findPopularsFilmsByGenreOrAndYear(Integer count, Long genreId, Integer year) {
        String sqlQuery =
                " SELECT *, mpa.name AS mpa_name FROM films AS f " +
                        " LEFT JOIN " +
                        "    (SELECT film_id, COUNT(film_id) AS likes_count " +
                        "     FROM LIKES " +
                        "     GROUP BY film_id " +
                        "     ) AS likes_by_film ON likes_by_film.film_id = f.film_id " +
                        " INNER JOIN mpa ON mpa.mpa_id = f.mpa_id " +
                        (genreId != null ? " INNER JOIN FILM_GENRE FG on FG.film_id = f.film_id AND fg.genre_id = " +
                                genreId : "") +
                        (year != null ? " WHERE (EXTRACT(YEAR FROM release_date)) = " + year : "") +
                        " ORDER BY likes_by_film.likes_count DESC " +
                        " LIMIT " + count;
        List<Film> listOfFilms = jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm);
        return listOfFilms;
    }


    @Override
    public List<Long> getRecommendations(Long firstUserId) {
        String sqlQuery = "SELECT user_id, count(*) as cnt FROM likes " +
                "WHERE film_id IN (" + SELECT_FILM_BY_FILM_ID + "))" +
                "   AND user_id != ?" +
                "GROUP BY user_id " +
                "ORDER BY cnt DESC " +
                "LIMIT 1";

        Long secondUserId;
        try {
            secondUserId = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> rs.getLong("user_id")
                    , firstUserId, firstUserId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }

        sqlQuery = SELECT_FILM_BY_FILM_ID +
                " AND film_id NOT IN (SELECT film_id  FROM likes WHERE user_id = ?))";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("film_id"), secondUserId, firstUserId);
    }

}
