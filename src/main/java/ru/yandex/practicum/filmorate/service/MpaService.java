package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {

    private final MpaDao mpaDao;

    public Mpa findMpaById(Long id) {
        log.info("F10-1. Получение рейтинга MPA по id");
        return mpaDao.findMpaById(id);
    }

    public List<Mpa> findAllMpa() {
        log.info("F10-2. Получение всех рейтингов MPA");
        return mpaDao.findAllMpa();
    }
}
