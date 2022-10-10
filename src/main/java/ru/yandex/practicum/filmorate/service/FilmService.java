package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    public Film getFilmById(Long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }

    public List<Film> findPopularFilms(Integer count) {
        return inMemoryFilmStorage.findPopularFilms(count);
    }

    public Film create(Film film) {
        return inMemoryFilmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public void deleteFilm(Long id) {
        inMemoryFilmStorage.deleteFilm(id);
    }

    public void addLikeToFilm(Long id, Long userId) {
        if (inMemoryFilmStorage.getFilms().containsKey(id) || inMemoryUserStorage.getUsers().containsKey(userId)) {
            if(inMemoryFilmStorage.getFilms().get(id).getLikes().contains(userId)) {
                throw new ValidationException("Пользователь уже ставил лайк к этому фильму.");
            }
            inMemoryFilmStorage.getFilms().get(id).getLikes().add(userId);
        }
    }

    public void deleteLikeFromFilm(Long id, Long userId) {
        if(!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Такого пользователя не существует");
        }
        if(!inMemoryFilmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new ValidationException("Пользователь не ставил лайк к этому фильму.");
        }
        if (inMemoryFilmStorage.getFilms().containsKey(id) && inMemoryUserStorage.getUsers().containsKey(userId)) {
            inMemoryFilmStorage.getFilms().get(id).getLikes().remove(userId);
        }
    }

}

