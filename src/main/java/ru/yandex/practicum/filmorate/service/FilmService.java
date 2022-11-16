package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final GenreDao genreDao;


    public List<Film> findAll(){
        List<Film> allFilms = filmDao.findAll();
        Map<Long, List<Genre>> filmGenres = genreDao.getGenresByFilm();
        for (Film film : allFilms) {
            List<Genre> genres = filmGenres.getOrDefault(film.getId(), new ArrayList<>());
            film.setGenres(genres);
        }
        return allFilms;
    }

    public Film getFilmById(Long filmId) {
        Film film = filmDao.getFilmById(filmId);
        //genreDao.addGenresToFilm(Objects.requireNonNull(film));
        genreDao.addGenresToFilm(film);
        return film;
    }

    public Film createFilm(Film film) {
        filmDao.createFilm(film);
        return genreDao.updateFilmGenres(film);
    }

    public Film updateFilm(Film film) {
        filmDao.updateFilm(film);
        return genreDao.updateFilmGenres(film);
    }

    public boolean deleteFilm(Long filmId) {
        return filmDao.deleteFilm(filmId);
    }

}

