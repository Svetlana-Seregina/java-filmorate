package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
public class MpaService {

    private final MpaDaoImpl mpaDaoImpl;

    @Autowired
    public MpaService(MpaDaoImpl mpaDaoImpl) {
        this.mpaDaoImpl = mpaDaoImpl;
    }

    public Mpa findMpaById(Long id) {
        log.info("Запрос на mpa_id = " + id);
        return mpaDaoImpl.findMpaById(id);
    }

    public List<Mpa> findAllMpa() {
        log.info("Список всех MPA: " + mpaDaoImpl.findAllMpa());
        return mpaDaoImpl.findAllMpa();
    }

}
