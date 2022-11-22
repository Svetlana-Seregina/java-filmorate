package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(Long filmId) {
        String sqlFilmRow = "SELECT *, mpa.NAME as mpa_name FROM FILMS " +
                "INNER JOIN mpa ON mpa.MPA_ID = films.MPA_ID WHERE FILM_ID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilmRow, FilmDaoImpl::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Фильм с film_id=%d не найден", filmId));
        }
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID) " +
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
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }


    @Override
    public void updateFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET " +
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
            throw new EntityNotFoundException("Фильм не найден, film id = " + film.getId());
        }
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT film_id, films.name, description, release_date, duration, rate, " +
                "films.mpa_id, mpa.NAME AS mpa_name" +
                " FROM films" +
                " INNER JOIN mpa ON mpa.MPA_ID = films.MPA_ID";

        return jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm);
    }

    @Override
    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        String sqlQuery;
        if (sortBy.equals("year")) {
            sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, " +
                    "f.duration, f.rate, f.mpa_id, mpa.name AS mpa_name " +
                    "FROM film_directors " +
                    "LEFT JOIN films AS f ON film_directors.film_id = f.film_id " +
                    "LEFT JOIN mpa ON mpa.mpa_id = f.mpa_id " +
                    "WHERE director_id = ?" +
                    "ORDER BY release_date";
        } else if (sortBy.equals("likes")) {
            sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, " +
                    "f.duration, f.rate, f.mpa_id, mpa.name AS mpa_name, likes_by_film.likes_count " +
                    "FROM film_directors " +
                    "LEFT JOIN films AS f ON film_directors.film_id = f.film_id " +
                    "LEFT JOIN (SELECT film_id, COUNT(film_id) AS likes_count " +
                    "FROM likes GROUP BY film_id) AS likes_by_film ON likes_by_film.film_id = f.film_id " +
                    "LEFT JOIN mpa ON mpa.mpa_id = f.mpa_id " +
                    "WHERE director_id = ?" +
                    "ORDER BY likes_by_film.likes_count DESC";
        } else {
            log.debug("Такая сортировка не поддерживается");
            return new ArrayList<>();
        }
        List<Film> sortedFilms = jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm, directorId);
        if (sortedFilms.isEmpty()) {
            throw new EntityNotFoundException("У режиссера с id = " + directorId + " нет фильмов");
        }
        return sortedFilms;

    }

    @Override
    public List<Film> searchFilmsBySubstring(String query, String by) {

        String where = Arrays.stream(by.split(","))
                .map(searchBy -> {
                    if (searchBy.equals("director")) {
                        return "DIRECTORS.name";
                    } else if (searchBy.equals("title")) {
                        return "FILMS.name";
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .distinct()
                .map(column -> "LOWER(" + column +") LIKE '%" + query.toLowerCase() + "%'")
                .collect(Collectors.joining( " AND " ));

        String sqlFilmRow = "SELECT *, mpa.NAME AS mpa_name FROM FILMS "+
                "LEFT JOIN DIRECTORS on FILMS.DIRECTOR_ID = DIRECTORS.DIRECTOR_ID " +
                "LEFT JOIN (SELECT FILM_ID, COUNT(FILM_ID) AS likes_count FROM LIKES GROUP BY FILM_ID) " +
                "AS likes_by_film ON likes_by_film.FILM_ID = FILMS.FILM_ID " +
                "INNER JOIN mpa ON mpa.MPA_ID = FILMS.MPA_ID " +
                "WHERE " + where +
                " ORDER BY likes_by_film.likes_count DESC";

        List<Film> films = jdbcTemplate.query(sqlFilmRow, FilmDaoImpl::mapRowToFilm);

        return films;
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
}
