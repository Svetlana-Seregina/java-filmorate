package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

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
    public List<Film> findPopularsFilmsByGenreOrAndYear(Integer count, Long genreId, Integer year) {
        String sqlQuery =
                " SELECT *, mpa.NAME AS mpa_name FROM FILMS AS f " +
                " LEFT JOIN " +
                "    (SELECT FILM_ID, COUNT(FILM_ID) AS likes_count " +
                "     FROM LIKES " +
                "     GROUP BY FILM_ID " +
                "     ) AS likes_by_film ON likes_by_film.FILM_ID = f.FILM_ID " +
                " INNER JOIN mpa ON mpa.MPA_ID = f.MPA_ID " +
                (genreId != null ? " INNER JOIN FILM_GENRE FG on FG.FILM_ID = f.FILM_ID AND fg.GENRE_ID = " + genreId : "") +
                (year != null ? " WHERE (EXTRACT(YEAR FROM RELEASE_DATE)) = " + year : "") +
                " ORDER BY likes_by_film.likes_count DESC " +
                " LIMIT " + count;
        List<Film> listOfFilms = jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm);
        return listOfFilms;
    }

}
