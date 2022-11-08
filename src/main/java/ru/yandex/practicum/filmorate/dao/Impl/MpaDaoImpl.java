package ru.yandex.practicum.filmorate.dao.Impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Mpa findMpaById(Long id) {
        String sqlMpa = "SELECT * FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.queryForObject(sqlMpa, this::mpaRowToMpa, id);
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
