package ru.yandex.practicum.filmorate.dao.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

// DAO-класс для доступа ко всем фильмам
// Остальные компоненты приложения будут обращаться к БД через этот DAO-класс, а не напрямую.
// Именно в нем будут выполняться запросы к базе с помощью клиента JdbcTemplate.
// Клиент вернёт исходные данные для формирования объектов бизнес-логики приложения.
// Полученные данные будут соответствовать данным из таблицы данного DAO-класса.
@Component
@Qualifier("filmDbStorage")
@Repository
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(Long id) {
        String sqlFilmRow = "SELECT *, mpa.NAME as mpa_name FROM FILMS" +
                " INNER JOIN mpa ON mpa.MPA_ID = films.MPA_ID" +
                " WHERE FILM_ID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilmRow, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Фильм с film_id=%d не найден", id));
        }
        String sqlSelectFilmGenres = "SELECT g.genre_id, g.name FROM film_genre" +
                " INNER JOIN genres AS g ON g.genre_id = film_genre.genre_id " +
                " WHERE film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlSelectFilmGenres,
                GenreDaoImpl::genreRowToGenre, id);
        Objects.requireNonNull(film).setGenres(genres);
        return film;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
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
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);

        List<Genre> genres = film.getGenres();

        if (film.getGenres() != null) {
            for (Genre genre : genres) {
                String sql = "INSERT INTO FILM_GENRE values (?, ?)";
                jdbcTemplate.update(sql, id, genre.getId());
            }
        }
        return film;
    }

    @Override
    public boolean deleteFilm(Long id) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ?" +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRate()
                , film.getMpa().getId()
                , film.getId());
        List<Genre> genres = film.getGenres();
        if (film.getGenres() != null) {
            sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQuery, film.getId());
            for (Genre genre : genres) {
                String sql = "MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) values (?, ?)";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
        return getFilmById(film.getId());

    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT film_id, films.name, description, release_date, duration, rate, films.mpa_id, mpa.NAME AS mpa_name" +
                " FROM films" +
                " INNER JOIN mpa ON mpa.MPA_ID = films.MPA_ID ";
        Map<Long, Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm)
                .stream()
                .collect(Collectors.toMap(Film::getId, f -> f));

        String sqlAllFilmGenres = "SELECT * FROM film_genre " +
                "INNER JOIN GENRES G ON FILM_GENRE.GENRE_ID = G.GENRE_ID";

        jdbcTemplate.query(sqlAllFilmGenres, (rs, rowId) -> {
            Genre g = GenreDaoImpl.genreRowToGenre(rs, rowId);
            Long film_id = rs.getLong("film_id");
            films.get(film_id).getGenres().add(g);
            return null;
        });

        return new ArrayList<>(films.values());
    }


    @Override
    public List<Film> findPopularFilms(Integer count) {
        String sqlQuery = "SELECT *, mpa.NAME AS mpa_name FROM FILMS AS f " +
                "LEFT JOIN " +
                "    (SELECT FILM_ID, COUNT(FILM_ID) AS likes_count " +
                "     FROM LIKES " +
                "     GROUP BY FILM_ID " +
                "     ) AS likes_by_film ON likes_by_film.FILM_ID = f.FILM_ID " +
                " INNER JOIN mpa ON mpa.MPA_ID = f.MPA_ID " +
                " ORDER BY likes_by_film.likes_count DESC " +
                " LIMIT " + count;

        List<Film> listOfFilms = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        return listOfFilms;
    }


    @Override
    public void addLikeToFilm(Long id, Long userId) {
        String sqlQuery = "INSERT INTO LIKES (USER_ID, FILM_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, id);
    }

    @Override
    public void removeLikeFromFilm(Long id, Long userId) {
        String sql = String.format("delete from LIKES where FILM_ID = %d and USER_ID = %d", id, userId);
        jdbcTemplate.update(sql);
    }
}
