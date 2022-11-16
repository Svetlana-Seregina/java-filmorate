package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final GenreDao genreDao;
    private final LikeDao likeDao;

    public List<Film> findPopularFilms(Integer count) {
        List<Film> allFilms = likeDao.findPopularFilms(count);
        for (Film film : allFilms) {
            genreDao.addGenresToFilm(film);
        }
        return allFilms;
    }

    public boolean addLikeToFilm(Long id, Long userId){
        return likeDao.addLikeToFilm(id, userId);
    }

    public void removeLikeFromFilm(Long id, Long userId){
        if (!likeDao.removeLikeFromFilm(id, userId)) {
            throw new EntityNotFoundException("Нет лайков у фильма " + id);
        }
    }

}
