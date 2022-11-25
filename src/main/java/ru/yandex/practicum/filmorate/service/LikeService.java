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
    private final FilmService filmService;


    public List<Film> findPopularsFilmsByGenreOrAndYear(Integer count, Long genreId, Integer year) {
        List<Film> popularFilms = likeDao.findPopularsFilmsByGenreOrAndYear(count, genreId, year);
        log.info(String.valueOf(popularFilms));
        genreDao.loadFilmsGenres(popularFilms);
        directorDao.loadFilmsDirectors(popularFilms);
        return popularFilms;
    }


    public boolean addLikeToFilm(Long id, Long userId){
        return likeDao.addLikeToFilm(id, userId);
    }

    public void removeLikeFromFilm(Long id, Long userId){
        if (!likeDao.removeLikeFromFilm(id, userId)) {
            throw new EntityNotFoundException("Нет лайков у фильма " + id);
        }
    }

    public List<Film> getRecommendations (Long userId) {
        List<Film> films = new ArrayList<>();
        likeDao.getRecommendations(userId).forEach(t -> films.add(filmService.getFilmById(t)));
        return films;
    }
}
