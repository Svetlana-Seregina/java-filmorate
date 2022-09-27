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
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;
    public Integer getNextId() {
        return nextId++;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        if (film == null) {
            throw new ValidationException("Передан пустой запрос");
        } else {
            validationFilm(film);
            film.setId(getNextId());
            films.put(film.getId(), film);
        }
        log.info("Название фильма: {}, Описание фильма: {}, Дата выхода на экран: {}, Продолжительность фильма: {}",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    @PutMapping("/films")
    public Film createAndUpdateFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            validationFilm(film);
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("такой id отсутствует");
        }
        log.info("Название фильма: {}, Описание фильма: {}, Дата выхода на экран: {}, Продолжительность фильма: {}",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    private void validationFilm (Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() >= MAX_NUMBER_OF_CHARACTERS) {
            throw new ValidationException("Максимальная длина описания — " + MAX_NUMBER_OF_CHARACTERS + "символов");
        } else if (!film.getReleaseDate().isAfter(MOVIES_BIRTHDAY)) {
            throw new ValidationException("Дата релиза — не может быть раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
