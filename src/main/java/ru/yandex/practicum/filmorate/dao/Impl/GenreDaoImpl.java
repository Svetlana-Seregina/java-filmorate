package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findGenreById(Long id) {
        String sqlGenre = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        log.info("Найден жанр с genre_id = " + id);
        return jdbcTemplate.queryForObject(sqlGenre, GenreDaoImpl::genreRowToGenre, id);
    }

    @Override
    public List<Genre> findAllGenre() {
        return jdbcTemplate.query("SELECT * FROM GENRES", GenreDaoImpl::genreRowToGenre);
    }

    public static Genre genreRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

}
