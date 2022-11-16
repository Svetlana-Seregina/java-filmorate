package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@RequestMapping("/films")
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final LikeService likeService;


    @GetMapping("/{filmId}")
    public Film findFilmById(@PathVariable Long filmId) {
        return filmService.getFilmById(filmId);
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
    public List<Film> findPopularsFilms(@Positive @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Получен запрос к эндпоинту на получение фильмов: count = " + count + " {}", likeService.findPopularFilms(count));
        return likeService.findPopularFilms(count);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public boolean addLikeToFilm(@PathVariable Long filmId, @PathVariable Long userId) {
        return likeService.addLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable Long filmId, @PathVariable Long userId) {
        likeService.removeLikeFromFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}")
    public boolean deleteFilm(@PathVariable Long filmId) {
        return filmService.deleteFilm(filmId);
    }

}
