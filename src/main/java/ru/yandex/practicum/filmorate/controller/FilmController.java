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
        log.info("Обрабатываем запрос на получение фильма с id = " + id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Обрабатываем запрос на создание фильма " + film);
        return filmService.createFilm(film);
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Обрабатываем запрос на получение списка всех фильмов");
        return filmService.findAll();
    }


    // GET /films/popular?count={limit}&genreId={genreId}&year={year}
    @GetMapping("/popular")
    public List<Film> findPopularsFilmsByGenreOrAndYear
            (@Positive @RequestParam(value = "count", defaultValue = "10", required = false) Integer count,
             @RequestParam(value = "genreId", required = false) Long genreId,
             @RequestParam (value = "year", required = false) Integer year) {

        log.info("Обрабатываем запрос на получение фильмов: count = {}, жанр id = {}, год = {} ",
                count, genreId, year);
        return likeService.findPopularsFilmsByGenreOrAndYear(count, genreId, year);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обрабатываем запрос на обновление фильма " + film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Обрабатываем запрос на добавление лайка фильму с id = {} от пользователя с id = {}",
                id, userId);
        return likeService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Обрабатываем запрос на удаление лайка у фильма с id = {} от пользователя с id = {}",
                id, userId);
        likeService.removeLikeFromFilm(id, userId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteFilm(@PathVariable Long id) {
        log.info("Обрабатываем запрос на удаление фильма с id = " + id);
        return filmService.deleteFilm(id);
    }


    //GET /films/search?query=крад&by=director,title
    @GetMapping("/search")
    public List<Film> searchFilmsBySubstring(@RequestParam(value = "query") String query,
                                  @RequestParam(value = "by", required = false, defaultValue = "title") String by) {
        log.info("Обрабатываем запрос на поиск фильма по строке: " + query);
        return filmService.searchFilmsBySubstring(query, by);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable Long directorId, @RequestParam String sortBy) {
        log.info("Обрабатываем запрос на получение списка фильмов режиссера с id = {}, отсортированных по признаку {}",
                directorId ,sortBy);
        return filmService.getFilmsByDirector(directorId, sortBy);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        log.info("Обрабатываем запрос на получение общих фильмов для пользователей с id = {} и id = {}",
                userId, friendId);
        return filmService.getCommonFilms(userId, friendId);
    }
}
