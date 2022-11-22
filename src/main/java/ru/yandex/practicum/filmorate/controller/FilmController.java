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
import java.util.List;

@RequestMapping("/films")
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final LikeService likeService;

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Обрабатываем запрос на создание фильма " + film);
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

    @PutMapping("/{id}/like/{userId}")
    public boolean addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        return likeService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        likeService.removeLikeFromFilm(id, userId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteFilm(@PathVariable Long id) {
        return filmService.deleteFilm(id);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable Long directorId, @RequestParam String sortBy) {
        log.debug("Обрабатываем запрос на получение списка фильмов режиссера с id " + directorId +
                " отсортированных по признаку " + sortBy);
        return filmService.getFilmsByDirector(directorId, sortBy);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        log.debug("Обрабатываем запрос на получение общих фильмов для пользователей " + userId + " и " + friendId);
        return filmService.getCommonFilms(userId, friendId);
    }
}
