package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class DirectorDaoImpl implements DirectorDao {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director createDirector(Director director) {
        String sqlQuery = "INSERT INTO directors(name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        return getDirectorById(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public Director updateDirector(Director director) {
        String sqlQuery = "UPDATE directors SET name = ? WHERE director_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                director.getName(),
                director.getId());
        return getDirectorById(director.getId());
    }

    @Override
    public Director getDirectorById(Long directorId) {
        try {
            String sqlDirector = "SELECT * FROM directors WHERE director_id = ?";
            return jdbcTemplate.queryForObject(sqlDirector, this::directorRowToDirector, directorId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Запрашиваемый режиссер с id=%d не найден.", directorId));
        }
    }

    @Override
    public List<Director> getAll() {
        return jdbcTemplate.query("SELECT * FROM directors", this::directorRowToDirector);
    }

    @Override
    public boolean deleteDirector(Long directorId) {
        String sqlQuery = "DELETE FROM directors WHERE director_id = ?";
        return jdbcTemplate.update(sqlQuery, directorId) > 0;
    }


    @Override
    public void addDirectorsToFilm(Film film) {
        String sqlSelectFilmDirectors = "SELECT directors.director_id, directors.name " +
                "FROM film_directors " +
                "INNER JOIN directors ON directors.director_id = film_directors.director_id " +
                "WHERE film_id = ?";
        List<Director> directors = jdbcTemplate.query(sqlSelectFilmDirectors,
                this::directorRowToDirector, film.getId());
        film.setDirectors(directors);
    }


    @Override
    public void loadFilmsDirectors(List<Film> films) {
        Map<Long, List<Director>> allFilmsDirectors = new HashMap<>();
        SqlRowSet directorInfoRows = jdbcTemplate.queryForRowSet("SELECT * FROM film_directors INNER JOIN directors " +
                "on film_directors.director_id=directors.director_id");
        while (directorInfoRows.next()) {

            long filmId = directorInfoRows.getLong("film_id");

            if (!allFilmsDirectors.containsKey(filmId)) {
                List<Director> filmDirectors = new ArrayList<>();
                filmDirectors.add(Director.builder()
                        .id(directorInfoRows.getLong("director_id"))
                        .name(directorInfoRows.getString("name"))
                        .build());
                allFilmsDirectors.put(filmId, filmDirectors);
            } else {
                allFilmsDirectors.get(filmId).add(Director.builder()
                        .id(directorInfoRows.getLong("director_id"))
                        .name(directorInfoRows.getString("name"))
                        .build());
            }
        }
        for (Film film : films) {
            if (allFilmsDirectors.containsKey(film.getId())) {
                film.setDirectors(allFilmsDirectors.get(film.getId()));
            }
        }
    }


    @Override
    public Film updateFilmDirectors(Film film) {
        List<Director> filmDirectors = film.getDirectors();
        if (filmDirectors == null) {
            deleteFilmDirectors(film);
            film.setDirectors(new ArrayList<>());
            return film;
        }
        filmDirectors = filmDirectors.stream().distinct().collect(Collectors.toList());
        Long filmId = film.getId();
        deleteFilmDirectors(film);

        List<Object[]> args = filmDirectors.stream()
                .map(director -> new Object[]{filmId, director.getId()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO film_directors (FILM_ID, DIRECTOR_ID) VALUES (?, ?)", args);
        film.setDirectors(filmDirectors);
        return film;
    }

    @Override
    public void deleteFilmDirectors(Film film) {
        String sqlQuery = "DELETE FROM FILM_DIRECTORS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }


    private Director directorRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getLong("director_id"))
                .name(resultSet.getString("name"))
                .build();
    }

}
