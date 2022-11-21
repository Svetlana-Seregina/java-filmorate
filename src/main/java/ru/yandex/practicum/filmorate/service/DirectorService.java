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

    public Director createDirector(Director director){
        return directorDao.createDirector(director);
    }

    public Director updateDirector(Director director){
        return directorDao.updateDirector(director);

    }

    public Director getDirectorById(Long directorId){
        return directorDao.getDirectorById(directorId);

    }

    public List<Director> getAll(){
        return directorDao.getAll();
    }

    public boolean deleteDirector(Long directorId){
        return directorDao.deleteDirector(directorId);
    }

}
