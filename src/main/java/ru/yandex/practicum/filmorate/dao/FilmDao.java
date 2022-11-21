package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {

    Film getFilmById(Long filmId);
    Film createFilm(Film film);
    boolean deleteFilm(Long filmId);
    void updateFilm(Film film);
    List<Film> findAll();
    List<Film> getFilmsByDirector(Long directorId, String sortType);

}
