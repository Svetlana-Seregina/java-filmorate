package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {

    Genre findGenreById(Long id);
    List<Genre> findAllGenre();
    void addGenresToFilm(Film film);
    void loadFilmsGenres(List<Film> films);
    Film updateFilmGenres(Film film);
    void deleteFilmGenres(Film film);

}
