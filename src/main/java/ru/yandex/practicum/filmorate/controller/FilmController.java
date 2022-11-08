package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.Impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/films")
@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private final UserDbStorage userDbStorage;
    @Autowired
    public FilmController(FilmService filmService, UserDbStorage userDbStorage) {
        this.filmService = filmService;
        this.userDbStorage = userDbStorage;
    }
    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }


    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", filmService.findAll().size());
        return filmService.findAll();
    }

    @GetMapping("/popular")
    public List<Film> findTenPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new ValidationException("Параметр count имеет отрицательное значение или равен 0: " + count);
        }
       log.info("Получен запрос к эндпоинту на получение фильмов: count = " + count + " {}", filmService.findPopularFilms(count));
        return filmService.findPopularFilms(count);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        try {
            userDbStorage.findUserById(userId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Такого пользователя не существует");
        }
        try {
            findFilmById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Такого фильма не существует");
        }
        filmService.removeLikeFromFilm(id, userId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteFilm(@PathVariable Long id) {
        return filmService.deleteFilm(id);
    }

}
