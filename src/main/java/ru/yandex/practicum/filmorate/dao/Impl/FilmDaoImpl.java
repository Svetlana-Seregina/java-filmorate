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
        String sqlQuery = "SELECT film_id, films.name, description, release_date, duration, rate, films.mpa_id, mpa.NAME AS mpa_name" +
                " FROM films" +
                " INNER JOIN mpa ON mpa.MPA_ID = films.MPA_ID";

        return jdbcTemplate.query(sqlQuery, FilmDaoImpl::mapRowToFilm);
    }

    @Override
    public List<Film> getFilmsByDirector(Long directorId, String sortType){
        if (sortType.equals("year")){

        } else if(sortType.equals("likes")){

        } else {

        }

        return new ArrayList<>();
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
                .build();
    }

}
