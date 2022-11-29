package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final GenreDao genreDao;
    private final LikeDao likeDao;
    private final DirectorDao directorDao;
    private final EventFeedService eventFeedService;
    private final FilmService filmService;

    public List<Film> findPopularsFilmsByGenreOrAndYear(Integer count, Long genreId, Integer year) {
        log.info("F3-3. Получение списка популярных фильмов по жанру и/или по году");
        List<Film> popularFilms = likeDao.findPopularsFilmsByGenreOrAndYear(count, genreId, year);
        genreDao.loadFilmsGenres(popularFilms);
        directorDao.loadFilmsDirectors(popularFilms);
        return popularFilms;
    }

    public boolean addLikeToFilm(Long filmId, Long userId) {
        log.info("F3-1. Добавление лайка фильму");
        boolean isAddLike = likeDao.addLikeToFilm(filmId, userId);
        if (isAddLike) {
            eventFeedService.addLikeEvent(userId, filmId);
        }
        return isAddLike;
    }

    public void removeLikeFromFilm(Long filmId, Long userId) {
        log.info("F3-2. Удаление лайка у фильма");
        if (likeDao.removeLikeFromFilm(filmId, userId)) {
            eventFeedService.removeLikeEvent(userId, filmId);
        } else {
            throw new EntityNotFoundException(
                    String.format("Не найдена запись лайка фильму с id=%d от пользователя с id=%d", filmId, userId));
        }
    }

    public List<Film> getRecommendations(Long userId) {
        log.info("F3-4. Получение рекомендации фильмов для пользователя");
        List<Film> films = new ArrayList<>();
        likeDao.getRecommendations(userId).forEach(t -> films.add(filmService.getFilmById(t)));
        return films;
    }
}
