package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController = new FilmController();

    @Test
    void findAll() {
        Film film = new Film(0, "Cramer vs Cramer", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), Duration.ofMinutes(105));

        assertEquals(film, filmController.create(film));
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    void findAllEmptySet() {
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    void create() {
        Film film = new Film(0, "Cramer vs Cramer", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), Duration.ofMinutes(105));
        assertEquals(film.getDescription().length(), filmController.create(film).getDescription().length());
    }
    @Test
    void createFilmWithoutOrBlankName() {
        Film film1 = new Film(0, null, "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), Duration.ofMinutes(105));
        assertThrows(ValidationException.class,
                () -> filmController.create(film1));

        Film film2 = new Film(0, "", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), Duration.ofMinutes(105));
        assertThrows(ValidationException.class,
                () -> filmController.create(film2));
    }

    @Test
    void createFilmWithNotLongDescription() {
        Film film = new Film(0, null, "There are three sides to this love story! " +
                "There are three sides to this love story! There are three sides to this love story! " +
                "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), Duration.ofMinutes(105));
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }

    @Test
    void createFilmWithWrongReleaseDate() {
        Film film = new Film(0, null, "There are three sides to this love story!",
                LocalDate.of(1879, 12, 7), Duration.ofMinutes(105));
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }

    @Test
    void createFilmWithNegativeDuration() {
        Film film = new Film(0, null, "There are three sides to this love story!",
                LocalDate.of(1879, 12, 7), Duration.ofMinutes(-105));
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }

}