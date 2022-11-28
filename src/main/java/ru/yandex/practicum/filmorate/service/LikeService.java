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
        List<Film> popularFilms = likeDao.findPopularsFilmsByGenreOrAndYear(count, genreId, year);
        genreDao.loadFilmsGenres(popularFilms);
        directorDao.loadFilmsDirectors(popularFilms);
        log.info("На запрос поиска фильмов по жанру и году получили список = {}", popularFilms);
        return popularFilms;
    }

    public boolean addLikeToFilm(Long filmId, Long userId){
        boolean isAddLike = likeDao.addLikeToFilm(filmId, userId);
        if (isAddLike) {
            eventFeedService.addLikeEvent(userId, filmId);
        }
        return isAddLike;
    }

    public void removeLikeFromFilm(Long filmId, Long userId){
        if (likeDao.removeLikeFromFilm(filmId, userId)) {
            eventFeedService.removeLikeEvent(userId, filmId);
        } else {
            throw new EntityNotFoundException("Нет лайков у фильма " + filmId);
        }
    }

    public List<Film> getRecommendations (Long userId) {
        List<Film> films = new ArrayList<>();
        likeDao.getRecommendations(userId).forEach(t -> films.add(filmService.getFilmById(t)));
        return films;
    }
}
