package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface DirectorDao {

    Director createDirector(Director director);

    Director updateDirector(Director director);

    Director getDirectorById(Long directorId);

    List<Director> getAll();

    boolean deleteDirector(Long directorId);

    void addDirectorsToFilm(Film film);

    void loadFilmsDirectors(List<Film> films);

    Film updateFilmDirectors(Film film);

    void deleteFilmDirectors(Film film);
}
