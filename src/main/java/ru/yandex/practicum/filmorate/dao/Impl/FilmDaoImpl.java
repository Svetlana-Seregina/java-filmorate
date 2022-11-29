package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_FILM_FIELDS_BY_DIRECTOR = " f.film_id, f.name, f.description, f.release_date, " +
            "f.duration, f.rate, f.mpa_id, mpa.name AS mpa_name " +
            "FROM film_directors AS fd " +
            "LEFT JOIN films AS f ON fd.film_id = f.film_id ";
    private static final String LEFT_JOIN_LIKES_BY_FILM_ON_FILM_ID = "LEFT JOIN (SELECT film_id, COUNT(film_id) " +
            "AS likes_count " +
            "FROM likes GROUP BY film_id) AS likes_by_film " +
            "ON likes_by_film.film_id = f.film_id ";

    @Override
    public Film getFilmById(Long filmId) {
        String sqlFilmRow = "SELECT *, mpa.name as mpa_name FROM films " +
                "INNER JOIN mpa ON mpa.mpa_id = films.mpa_id WHERE film_id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilmRow, FilmDaoImpl::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Фильм с id=%d не найден", filmId));
        }
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO films(name, description, release_date, duration, rate, mpa_id) " +
                "values (?, ?, ?, ?, ?, ?)";
        //В тестах PostMan для создания режиссера нет поля рейтинг, поэтому добавил проверку на пустое значение
        if (film.getRate() == null) {
            film.setRate(0);
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, Math.toIntExact(film.getMpa().getId()));
            return stmt;
        }, keyHolder);

        long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(filmId);

        return film;
    }

    @Override
    public boolean deleteFilm(Long filmId) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }


    @Override
    public void updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? WHERE film_id = ?";

        int updatedRows = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRate()
                , film.getMpa().getId()
                , film.getId());

        if (updatedRows == 0) {
            throw new EntityNotFoundException(String.format("Фильм с id=%d не найден", film.getId()));
        }
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT film_id, films.name, description, release_date, duration, rate, " +
                "films.mpa_id, mpa.name AS mpa_name " +
                "FROM films " +
                "INNER JOIN mpa ON mpa.mpa_id = films.mpa_id";

        return jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm);
    }

    @Override
    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        String sqlQuery;
        if (sortBy.equals("year")) {
            sqlQuery = "SELECT " + SELECT_FILM_FIELDS_BY_DIRECTOR +
                    "LEFT JOIN mpa ON mpa.mpa_id = f.mpa_id " +
                    "WHERE director_id = ? " +
                    "ORDER BY release_date";
        } else if (sortBy.equals("likes")) {
            sqlQuery = "SELECT" + insertLikesCountField() + LEFT_JOIN_LIKES_BY_FILM_ON_FILM_ID +
                    "LEFT JOIN mpa ON mpa.mpa_id = f.mpa_id " +
                    "WHERE director_id = ? " +
                    "ORDER BY likes_by_film.likes_count DESC";
        } else {
            log.warn("Такой тип сортировки не поддерживается. Переданное значение: {}", sortBy);
            return new ArrayList<>();
        }
        List<Film> sortedFilms = jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm, directorId);
        if (sortedFilms.isEmpty()) {
            throw new EntityNotFoundException(String.format("У режиссера с id=%d нет фильмов", directorId));

        }
        return sortedFilms;
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String sqlQuery = "SELECT l1.film_id, f.name, f.description, f.release_date, " +
                "f.duration, f.mpa_id, f.rate, mpa.name as mpa_name, likes_by_film.likes_count " +
                "FROM likes as l1 " +
                "JOIN (SELECT * FROM likes WHERE user_id = ?) AS l2 on l1.film_id = l2.film_id " +
                "left join films as f on l1.film_id = f.film_id " +
                "left join mpa on f.mpa_id = mpa.mpa_id " +
                LEFT_JOIN_LIKES_BY_FILM_ON_FILM_ID +
                "WHERE l1.USER_ID = ? " +
                "ORDER BY likes_by_film.likes_count DESC";
        return jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm, friendId, userId);
    }

    @Override
    public List<Film> searchFilmsBySubstring(String query, String by) {

        String where = Arrays.stream(by.split(","))
                .map(searchBy -> {
                    if (searchBy.equals("director")) {
                        return "d.name";
                    } else if (searchBy.equals("title")) {
                        return "f.name";
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .distinct()
                .map(column -> "LOWER(" + column + ") LIKE '%" + query.toLowerCase() + "%'")
                .collect(Collectors.joining(" OR "));

        String sqlFilmRow = "SELECT *, mpa.name AS mpa_name FROM films AS f " +
                "LEFT JOIN film_directors AS fd on f.film_id = fd.film_id " +
                "LEFT JOIN directors AS d ON fd.director_id = d.director_id " +
                LEFT_JOIN_LIKES_BY_FILM_ON_FILM_ID +
                "INNER JOIN mpa ON mpa.mpa_id = f.mpa_id " +
                "WHERE " + where +
                " ORDER BY likes_by_film.likes_count DESC";
        return jdbcTemplate.query(sqlFilmRow, FilmDaoImpl::mapRowToFilm);
    }


    public static Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(Mpa.builder()
                        .id(resultSet.getLong("mpa_id"))
                        .name(resultSet.getString("mpa_name"))
                        .build())
                .genres(new ArrayList<>())
                .directors(new ArrayList<>())
                .build();
    }

    private static String insertLikesCountField() {
        int index = SELECT_FILM_FIELDS_BY_DIRECTOR.indexOf("FROM");
        String beginStr = SELECT_FILM_FIELDS_BY_DIRECTOR.substring(0,index);
        String endStr = SELECT_FILM_FIELDS_BY_DIRECTOR.substring(index);
        return beginStr + ", likes_by_film.likes_count " + endStr;
    }
}
