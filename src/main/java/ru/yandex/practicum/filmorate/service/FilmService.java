package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmDao filmStorage;

    public List<Film> findAll(){
        return filmStorage.findAll();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public boolean deleteFilm(Long id) {
        return filmStorage.deleteFilm(id);
    }

}

