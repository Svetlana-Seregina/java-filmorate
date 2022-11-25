package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    private final EventFeedService eventFeedService;
    private final FilmService filmService;

    public List<Film> findPopularFilms(Integer count) {
        List<Film> popularFilms = likeDao.findPopularFilms(count);
        genreDao.loadFilmsGenres(popularFilms);
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
