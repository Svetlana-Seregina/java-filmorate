package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DirectorService {

    private final DirectorDao directorDao;

    public Director createDirector(Director director) {
        log.info("F4-1. Создание режиссера");
        return directorDao.createDirector(director);
    }

    public Director updateDirector(Director director) {
        log.info("F4-2. Обновление режиссера");
        return directorDao.updateDirector(director);
    }

    public Director getDirectorById(Long directorId) {
        log.info("F4-3. Получение режиссера по id");
        return directorDao.getDirectorById(directorId);
    }

    public List<Director> getAll() {
        log.info("F4-4. Получение всех режиссеров");
        return directorDao.getAll();
    }

    public boolean deleteDirector(Long directorId) {
        log.info("F4-5. Удаление режиссера");
        return directorDao.deleteDirector(directorId);
    }
}
