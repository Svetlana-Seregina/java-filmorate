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
        log.info("F2-5. Получение всех фильмов");
        List<Film> allFilms = filmDao.findAll();
        genreDao.loadFilmsGenres(allFilms);
        directorDao.loadFilmsDirectors(allFilms);
        return allFilms;
    }

    public Film getFilmById(Long filmId) {
        log.info("F2-1. Получение фильма по id");
        Film film = filmDao.getFilmById(filmId);
        genreDao.addGenresToFilm(film);
        directorDao.addDirectorsToFilm(film);
        return film;
    }

    public Film createFilm(Film film) {
        log.info("F2-2. Создание фильма");
        filmDao.createFilm(film);
        directorDao.updateFilmDirectors(film);
        return genreDao.updateFilmGenres(film);
    }

    public Film updateFilm(Film film) {
        log.info("F2-4. Обновление фильма");
        filmDao.updateFilm(film);
        directorDao.updateFilmDirectors(film);
        return genreDao.updateFilmGenres(film);
    }

    public boolean deleteFilm(Long filmId) {
        log.info("F2-3. Удаление фильма");
        return filmDao.deleteFilm(filmId);
    }

    public List<Film> searchFilmsBySubstring(String query, String by) {
        log.info("F2-6. Получение фильма по подстроке поиска");
        List<Film> filmsList = filmDao.searchFilmsBySubstring(query, by);
        genreDao.loadFilmsGenres(filmsList);
        directorDao.loadFilmsDirectors(filmsList);
        return filmsList;
    }

    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        log.info("F2-7. Получение фильмов режиссера");
        List<Film> filmsByDirector = filmDao.getFilmsByDirector(directorId, sortBy);
        genreDao.loadFilmsGenres(filmsByDirector);
        directorDao.loadFilmsDirectors(filmsByDirector);
        return filmsByDirector;
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        log.info("F2-8. Получение общих фильмов для двух пользователей");
        return filmDao.getCommonFilms(userId, friendId);
    }
}
