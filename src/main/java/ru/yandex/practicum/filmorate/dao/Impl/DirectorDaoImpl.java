package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DirectorDaoImpl implements DirectorDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director createDirector(Director director) {
        String sqlQuery = "INSERT INTO directors(name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        long directorId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        director.setId(directorId);
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        String sqlQuery = "UPDATE directors SET name = ? WHERE director_id = ?";
        int updatedRows = jdbcTemplate.update(
                sqlQuery,
                director.getName(),
                director.getId());
        if (updatedRows == 0) {
            throw new EntityNotFoundException(String.format("Режиссер с id=%d не найден.", director.getId()));
        }
        return director;
    }

    @Override
    public Director getDirectorById(Long directorId) {
        try {
            String sqlDirector = "SELECT * FROM directors WHERE director_id = ?";
            return jdbcTemplate.queryForObject(sqlDirector, this::directorRowToDirector, directorId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Режиссер с id=%d не найден.", directorId));
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
        String sqlSelectFilmDirectors = "SELECT d.director_id, d.name " +
                "FROM film_directors AS fd  " +
                "INNER JOIN directors AS d ON d.director_id = fd.director_id " +
                "WHERE film_id = ?";
        List<Director> directors = jdbcTemplate.query(sqlSelectFilmDirectors,
                this::directorRowToDirector, film.getId());
        film.setDirectors(directors);
    }

    @Override
    public void loadFilmsDirectors(List<Film> films) {
        Map<Long, List<Director>> allFilmsDirectors = new HashMap<>();
        SqlRowSet directorInfoRows = jdbcTemplate.queryForRowSet("SELECT * FROM film_directors AS fd " +
                "INNER JOIN directors AS d " +
                "ON fd.director_id = d.director_id");
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
        deleteFilmDirectors(film);
        if (film.getDirectors() == null) {
            film.setDirectors(new ArrayList<>());
            return film;
        }
        List<Director> filmDirectors = film.getDirectors().stream().distinct().collect(Collectors.toList());
        List<Object[]> args = filmDirectors.stream()
                .map(director -> new Object[]{film.getId(), director.getId()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)", args);
        film.setDirectors(filmDirectors);
        return film;
    }

    @Override
    public void deleteFilmDirectors(Film film) {
        String sqlQuery = "DELETE FROM film_directors WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }


    private Director directorRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getLong("director_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
