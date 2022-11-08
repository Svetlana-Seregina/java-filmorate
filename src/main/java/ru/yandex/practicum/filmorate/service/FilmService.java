package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmDbStorage filmDbStorage;

    public List<Film> findAll(){
        return filmDbStorage.findAll();
    }

    public Film getFilmById(Long id) {
        return filmDbStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmDbStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.updateFilm(film);
    }

    public boolean deleteFilm(Long id) {
        return filmDbStorage.deleteFilm(id);
    }

    public List<Film> findPopularFilms(Integer count) {
        return filmDbStorage.findPopularFilms(count);
    }

    public void addLikeToFilm(Long id, Long userId){
        filmDbStorage.addLikeToFilm(id, userId);
    }

    public void removeLikeFromFilm(Long id, Long userId){
        filmDbStorage.removeLikeFromFilm(id, userId);
    }

}

