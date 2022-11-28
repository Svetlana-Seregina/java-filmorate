package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreDao genreDao;

    public Genre findGenreById(Long id) {
        log.info("F6-1. Получение жанра по id");
        return genreDao.findGenreById(id);
    }

    public List<Genre> findAllGenre() {
        log.info("F6-2. Получение всех жанров");
        return genreDao.findAllGenre();
    }
}
