package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import java.util.List;

@RequestMapping("/mpa")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa findMpaById(@PathVariable Long id) {
        log.info("Получен запрос на получение mpa_id = {}", id);
        return mpaService.findMpaById(id);
    }

    @GetMapping
    public List<Mpa> findAllMpa() {
        return mpaService.findAllMpa();
    }

}
