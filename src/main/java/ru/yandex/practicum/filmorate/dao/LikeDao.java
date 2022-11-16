package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeDao {

    boolean addLikeToFilm(Long id, Long userId);
    boolean removeLikeFromFilm(Long filmId, Long userId);
    List<Film> findPopularFilms(Integer count);

}
