package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RequestMapping("/mpa")
@RestController
public class MpaController {

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    public Mpa findMpaById(@PathVariable Long id) {
        if (id <= 0 || id > 5) {
            throw  new EntityNotFoundException(String.format("Запрашиваемый mpa_id = %d меньше 0 или больше 5 ", id));
        }
        return mpaService.findMpaById(id);
    }

    @GetMapping
    public List<Mpa> findAllMpa() {
        return mpaService.findAllMpa();
    }

}
