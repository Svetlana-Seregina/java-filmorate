package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    /*private FilmController filmController;


   @Test
    void findAll() {
        Film film = new Film("Cramer vs Cramer", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), 105);

        assertEquals(film, filmController.create(film));
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    void findAllEmptySet() {
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    void create() {
        Film film = new Film("Cramer vs Cramer", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), 105);
        assertEquals(105, filmController.create(film).getDuration());
    }

   @Test
    void createFilmWithWrongReleaseDate() {
        Film film = new Film(null, "There are three sides to this love story!",
                LocalDate.of(1879, 12, 7), 105);
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }

    @Test
    void createFilmWithNegativeDuration() {
        Film film = new Film(null, "There are three sides to this love story!",
                LocalDate.of(1879, 12, 7), -105);
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }*/

}