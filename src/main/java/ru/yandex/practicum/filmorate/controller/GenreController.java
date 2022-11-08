package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RequestMapping("/genres")
@RestController
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public Genre findGenreById(@PathVariable Long id) {
        if (id <= 0) {
            throw  new EntityNotFoundException(String.format("Запрашиваемый genre_id = %d меньше или равен нулю ", id));
        }
        return genreService.findGenreById(id);
    }

    @GetMapping
    public List<Genre> findAllGenre() {
        return genreService.findAllGenre();
    }
}
