package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j

public class FilmController {
    private static final LocalDate MOVIES_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private static final int MAX_NUMBER_OF_CHARACTERS = 200;
    private final Set<Film> films = new HashSet<>();

    @GetMapping("/films")
    public Set<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        log.info("Название фильма: {}, Описание фильма: {}, Дата выхода на экран: {}, Продолжительность фильма: {}",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > MAX_NUMBER_OF_CHARACTERS) {
            throw new ValidationException("Максимальная длина описания — " + MAX_NUMBER_OF_CHARACTERS + "символов");
        } else if (!film.getReleaseDate().isAfter(MOVIES_BIRTHDAY)) {
            throw new ValidationException("Дата релиза — не может быть раньше 28 декабря 1895 года");
        } else if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        films.add(film);
        return film;
    }

    @PutMapping("/films")
    public Film createAndUpdateFilm(@RequestBody Film film) {
        if (films.contains(film)) {
            if (film.getName() == null || film.getName().isBlank()) {
                throw new ValidationException("Название не может быть пустым");
            } else
                films.add(film);
        }
        return film;
    }


}
