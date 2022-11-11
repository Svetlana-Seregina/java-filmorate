package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeDao likeDao;
    public List<Film> findPopularFilms(Integer count) {
        return likeDao.findPopularFilms(count);
    }

    public void addLikeToFilm(Long id, Long userId){
        likeDao.addLikeToFilm(id, userId);
    }

    public void removeLikeFromFilm(Long id, Long userId){
        likeDao.removeLikeFromFilm(id, userId);
    }
}
