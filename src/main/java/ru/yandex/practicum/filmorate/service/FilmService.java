package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final DirectorDao directorDao;

    public List<Film> findAll() {
        List<Film> allFilms = filmDao.findAll();
        genreDao.loadFilmsGenres(allFilms);
        directorDao.loadFilmsDirectors(allFilms);
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
        directorDao.updateFilmDirectors(film);
        return genreDao.updateFilmGenres(film);
    }

    public Film updateFilm(Film film) {
        filmDao.updateFilm(film);
        directorDao.updateFilmDirectors(film);
        return genreDao.updateFilmGenres(film);
    }

    public boolean deleteFilm(Long filmId) {
        return filmDao.deleteFilm(filmId);
    }

   public List<Film> searchFilmsBySubstring(String query, String by) {
       log.info("Запрос на получение фильма по подстроке поиска = {}, среди = {}", query, by);
       List<Film> filmsList = filmDao.searchFilmsBySubstring(query, by);
       genreDao.loadFilmsGenres(filmsList);
       directorDao.loadFilmsDirectors(filmsList);
       return filmsList;
    }

    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        List<Film> filmsByDirector = filmDao.getFilmsByDirector(directorId, sortBy);
        genreDao.loadFilmsGenres(filmsByDirector);
        directorDao.loadFilmsDirectors(filmsByDirector);
        return filmsByDirector;
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return filmDao.getCommonFilms(userId, friendId);
    }
}
