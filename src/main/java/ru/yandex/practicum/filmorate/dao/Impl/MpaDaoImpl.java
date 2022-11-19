package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa findMpaById(Long id) {
        try {
            String sqlMpa = "SELECT * FROM MPA WHERE MPA_ID = ?";
            return jdbcTemplate.queryForObject(sqlMpa, this::mpaRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Запрашиваемый mpa_id меньше 0 или больше 5 = %d ", id));
        }
    }

    @Override
    public List<Mpa> findAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa", this::mpaRowToMpa);
    }

    private Mpa mpaRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
