package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
@Slf4j
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping
    public Director createDirector(@Valid @RequestBody Director director) {
        log.debug("Обрабатываем запрос на создание режиссера " + director);
        return directorService.createDirector(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        log.debug("Обрабатываем запрос на обновление режиссера " + director);
        return directorService.updateDirector(director);

    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Long id) {
        log.debug("Обрабатываем запрос на получение режиссера с id " + id);
        return directorService.getDirectorById(id);
    }

    @GetMapping
    public List<Director> getAll() {
        log.debug("Обрабатываем запрос на получение списка всех режиссеров");
        return directorService.getAll();
    }

    @DeleteMapping("/{id}")
    public boolean deleteDirector(@PathVariable Long id) {
        log.debug("Обрабатываем запрос на удаление режиссера с id " + id);
        return directorService.deleteDirector(id);
    }
}
