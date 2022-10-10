package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    // добавления, удаления и модификации объектов
    Film create(Film film);
    void deleteFilm(Long id);
    Film updateFilm(Film film);
    Collection<Film> findAll();
    Film getFilmById(Long id);
    List<Film> findPopularFilms(Integer count);
    Map<Long, Film> getFilms();

}
