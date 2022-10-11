package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;
    public static final LocalDate MOVIES_BIRTHDAY = LocalDate.of(1895, 12, 28);
    public Long getNextId() {
        return nextId++;
    }

    public Collection<Film> findAll() {
        log.info("Все фильмы: {}", films.values());
        return films.values();
    }

    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new EntityNotFoundException("Фильма с id " + id + "не существует");
        }
        log.info("Фильм по id: {}", films.get(id));
        return films.get(id);
    }

    public Map<Long, Film> getFilms() {
        return films;
    }


    @Override
    public Film create(Film film) {
        checkReleaseDateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("id фильма: {}, Название фильма: {}, Описание фильма: {}, Дата выхода на экран: {}, Продолжительность фильма: {}",
                film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            checkReleaseDateFilm(film);
            films.put(film.getId(), film);
        } else {
            throw new EntityNotFoundException("Фильм " + film.getName() + "не найдет");
        }
        log.info("id фильма: {}, Название фильма: {}, Описание фильма: {}, Дата выхода на экран: {}, Продолжительность фильма: {}",
                film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    private void checkReleaseDateFilm(Film film) {
        if (!film.getReleaseDate().isAfter(MOVIES_BIRTHDAY)) {
            throw new ValidationException("Дата релиза — не может быть раньше 28 декабря 1895 года");
        }
    }

    @Override
    public void deleteFilm(Long id) {
        if (films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new EntityNotFoundException("Фильм с таким id " + id + " отсутствует.");
        }
        log.info("id фильма: {}", films.get(id));
    }

    public List<Film> findPopularFilms(Integer count) {
        List<Film> popularFilms = new ArrayList<>(films.values());
        return popularFilms.stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());

    }

    private int compare(Film f0, Film f1) {
        return f1.getLikes().size() - f0.getLikes().size();
    }

}
