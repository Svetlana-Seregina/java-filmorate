package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreDaoImpl genreDaoImpl;

    @Autowired
    public GenreService(GenreDaoImpl genreDaoImpl) {
        this.genreDaoImpl = genreDaoImpl;
    }

    public Genre findGenreById(Long id) {
        log.info("Запрос на genre_id = " + id);
        return genreDaoImpl.findGenreById(id);
    }

    public List<Genre> findAllGenre() {
        log.info("Список всех жанров: " + genreDaoImpl.findAllGenre());
        return genreDaoImpl.findAllGenre();
    }
}
