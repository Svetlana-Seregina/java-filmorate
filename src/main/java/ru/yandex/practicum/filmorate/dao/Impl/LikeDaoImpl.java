package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
@RequiredArgsConstructor
@Component
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeToFilm(Long id, Long userId) {
        String sqlQuery = "INSERT INTO LIKES (USER_ID, FILM_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, id);
    }

    @Override
    public void removeLikeFromFilm(Long id, Long userId) {
        String sql = String.format("delete from LIKES where FILM_ID = %d and USER_ID = %d", id, userId);
        jdbcTemplate.update(sql);
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        String sqlQuery = "SELECT *, mpa.NAME AS mpa_name FROM FILMS AS f " +
                "LEFT JOIN " +
                "    (SELECT FILM_ID, COUNT(FILM_ID) AS likes_count " +
                "     FROM LIKES " +
                "     GROUP BY FILM_ID " +
                "     ) AS likes_by_film ON likes_by_film.FILM_ID = f.FILM_ID " +
                " INNER JOIN mpa ON mpa.MPA_ID = f.MPA_ID " +
                " ORDER BY likes_by_film.likes_count DESC " +
                " LIMIT " + count;

        List<Film> listOfFilms = jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm);
        return listOfFilms;
    }
}
