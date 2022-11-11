package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
public class MpaService {

    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa findMpaById(Long id) {
        log.info("Запрос на mpa_id = " + id);
        return mpaDao.findMpaById(id);
    }

    public List<Mpa> findAllMpa() {
        log.info("Список всех MPA: " + mpaDao.findAllMpa());
        return mpaDao.findAllMpa();
    }

}
