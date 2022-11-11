package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    // добавления, удаления и модификации объектов
    Film getFilmById(Long id);
    Film createFilm(Film film);
    boolean deleteFilm(Long id);
    Film updateFilm(Film film);
    List<Film> findAll();

}
