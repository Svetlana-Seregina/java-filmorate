package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre findGenreById(Long id) {
        try {
            String sqlGenre = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
            return jdbcTemplate.queryForObject(sqlGenre, GenreDaoImpl::genreRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("genre_id не может быть меньше нуля = " + id);
        }
    }

    @Override
    public List<Genre> findAllGenre() {
        return jdbcTemplate.query("SELECT * FROM GENRES", GenreDaoImpl::genreRowToGenre);
    }

    @Override
    public void addGenresToFilm(Film film) {
        String sqlSelectFilmGenres = "SELECT g.genre_id, g.name " +
                "FROM film_genre " +
                "INNER JOIN genres AS g ON g.genre_id = film_genre.genre_id " +
                "WHERE film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlSelectFilmGenres,
                GenreDaoImpl::genreRowToGenre, film.getId());
        film.setGenres(genres);
    }

    public void loadFilmsGenres(List<Film> films) {

        if (films.size() == 0) {
            return;
        }

        Map<Long, List<Genre>> genresByFilmId = new HashMap<>();

        List<String> filmIds = films.stream()
                .map(f -> f.getId().toString())
                .collect(Collectors.toList());

        jdbcTemplate.query("SELECT film_id, g.genre_id, g.name FROM FILM_GENRE " +
                        "INNER JOIN genres AS g ON g.genre_id = film_genre.genre_id " +
                        "WHERE film_id IN (" + String.join(",", filmIds) + ")",
                (rs, rowNum) -> {
                    long filmId = rs.getLong("film_id");
                    List<Genre> list = genresByFilmId.computeIfAbsent(filmId, id -> new ArrayList<>());
                    list.add(GenreDaoImpl.genreRowToGenre(rs, rowNum));
                    return null;
                });

        for (Film film : films) {
            List<Genre> genres = genresByFilmId.getOrDefault(film.getId(), new ArrayList<>());
            film.setGenres(genres);
        }
    }

    @Override
    public Film updateFilmGenres(Film film) {
        List<Genre> filmGenres = film.getGenres();
        if (filmGenres == null) {
            return film;
        }
        filmGenres = filmGenres.stream().distinct().collect(Collectors.toList());
        Long filmId = film.getId();
        deleteFilmGenres(film);

        List<Object[]> args = filmGenres.stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)", args);

        film.setGenres(filmGenres);
        return film;
    }

    @Override
    public void deleteFilmGenres(Film film) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    public static Genre genreRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
