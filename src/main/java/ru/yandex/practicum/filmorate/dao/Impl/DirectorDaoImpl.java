package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

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
    public void addDirectorsToFilm(Film film){
        String sqlSelectFilmDirectors = "SELECT director_id, d.name " +
                "FROM films " +
                "INNER JOIN directors AS d ON d.director_id = films.director_id " +
                "WHERE film_id = ?";
        List<Director> directors = jdbcTemplate.query(sqlSelectFilmDirectors,
                this::directorRowToDirector, film.getId());
        film.setDirectors(directors);
    }

    private Director directorRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getLong("director_id"))
                .name(resultSet.getString("name"))
                .build();
    }

}
