package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    // добавления, удаления и модификации объектов
    Film getFilmById(Long id);
    Film createFilm(Film film);
    boolean deleteFilm(Long id);
    Film updateFilm(Film film);
    List<Film> findAll();

    List<Film> findPopularFilms(Integer count);

    void addLikeToFilm(Long id, Long userId);
    void removeLikeFromFilm(Long id, Long userId);

}
