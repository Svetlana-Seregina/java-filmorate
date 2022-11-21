package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final GenreDao genreDao;

    private final DirectorDao directorDao;


    public List<Film> findAll(){
        List<Film> allFilms = filmDao.findAll();
        genreDao.loadFilmsGenres(allFilms);
        return allFilms;
    }

    public Film getFilmById(Long filmId) {
        Film film = filmDao.getFilmById(filmId);
        genreDao.addGenresToFilm(film);
        directorDao.addDirectorsToFilm(film);
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

    public List<Film> getFilmsByDirector(Long directorId, String sortType){
        return filmDao.getFilmsByDirector(directorId, sortType);
    }

}

