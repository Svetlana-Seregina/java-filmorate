package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeDao {

    void addLikeToFilm(Long id, Long userId);
    void removeLikeFromFilm(Long id, Long userId);
    List<Film> findPopularFilms(Integer count);
}
