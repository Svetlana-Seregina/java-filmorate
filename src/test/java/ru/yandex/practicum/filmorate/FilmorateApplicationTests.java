package ru.yandex.practicum.filmorate;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.Impl.*;
import ru.yandex.practicum.filmorate.model.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class FilmorateApplicationTests {
    private final UserDaoImpl userDaoImpl;
    private final FilmDaoImpl filmDaoImpl;
    private final GenreDaoImpl genreDaoImpl;
    private final MpaDaoImpl mpaDaoImpl;
    private final LikeDaoImpl likeDaoImpl;

    private final DirectorDaoImpl directorDaoImpl;

    @Test
    void contextLoads() {
    }

    @Test
    public void testFindUserById() {
        User user = userDaoImpl.createUser(User.builder()
                .name("Nick Name")
                .email("mail@mail.ru")
                .login("dolore")
                .birthday(LocalDate.of(2000, 11, 15))
                .build());

        assertThat(userDaoImpl.findUserById(user.getId()))
                .isNotNull()
                .isEqualTo(user);
    }


    @Test
    public void testUpdateUser() {
        User user2 = userDaoImpl.createUser(User.builder()
                .name("Nick Name5")
                .email("mail5@mail.ru")
                .login("dolore5")
                .birthday(LocalDate.of(2000, 11, 15))
                .build());
        log.info("Создан пользователь user с name = {}", user2.getName());
        user2 = userDaoImpl.updateUser(User.builder()
                .id(2L)
                .name("est adipisicing")
                .email("mail@yandex.ru")
                .login("doloreUpdate")
                .birthday(LocalDate.of(2000, 11, 15))
                .build());
        log.info("Пользователь user поменял имя на name = {}", user2.getName());
        assertThat(userDaoImpl.findUserById(user2.getId()).getName()).isEqualTo("est adipisicing");
    }

    @Test
    public void testFindAllUsers() {
        userDaoImpl.createUser(User.builder()
                .name("Nick Name2")
                .email("mail2@mail.ru")
                .login("dolore2")
                .birthday(LocalDate.of(2000, 11, 15))
                .build());
        userDaoImpl.createUser(User.builder()
                .name("Nick Name3")
                .email("mail3@mail.ru")
                .login("dolore3")
                .birthday(LocalDate.of(2000, 11, 15))
                .build());
        assertThat(userDaoImpl.findAllUsers().size())
                .isEqualTo(2);
    }

    @Test
    public void testUpdateFilmById() {
        Film film = filmDaoImpl.createFilm(Film.builder()
                .name("labore nulla")
                .description("Duis in consequat esse")
                .releaseDate(LocalDate.of(1979, 4, 17))
                .duration(140)
                .rate(4)
                .mpa(Mpa.builder().id(1L).name("G").build())
                .genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
                .build());
        genreDaoImpl.updateFilmGenres(film);
        filmDaoImpl.updateFilm(Film.builder()
                .id(1L)
                .name("Film Updated")
                .description("Duis in consequat esse")
                .releaseDate(LocalDate.of(1979, 4, 17))
                .duration(150)
                .rate(4)
                .mpa(Mpa.builder().id(1L).name("G").build())
                .genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
                .build());
        genreDaoImpl.updateFilmGenres(film);
        assertThat(filmDaoImpl.getFilmById(film.getId()))
                .hasFieldOrPropertyWithValue("name", "Film Updated");
    }

    @Test
    public void testGetFilmById() {
        Film film = filmDaoImpl.createFilm(Film.builder()
                .name("labore nulla 4")
                .description("Duis in consequat esse 4")
                .releaseDate(LocalDate.of(1979, 4, 17))
                .duration(140)
                .rate(4)
                .mpa(Mpa.builder().id(1L).name("G").build())
                .genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
                .directors(new ArrayList<>())
                .build());
        genreDaoImpl.addGenresToFilm(film);

        assertThat(filmDaoImpl.getFilmById(film.getId()))
                .isEqualTo(film);
    }

    @Test
    public void testFilmAddLike() {
        userDaoImpl.createUser(User.builder()
                .name("Nick Name4")
                .email("mail4@mail.ru")
                .login("dolore4")
                .birthday(LocalDate.of(2000, 11, 15))
                .build());
        userDaoImpl.createUser(User.builder()
                .name("Nick")
                .email("mail45@mail.ru")
                .login("dolores")
                .birthday(LocalDate.of(2000, 10, 15))
                .build());

        filmDaoImpl.createFilm(Film.builder()
                .name("labore nulla")
                .description("Duis in consequat esse")
                .releaseDate(LocalDate.of(1979, 4, 17))
                .duration(140)
                .rate(4)
                .mpa(Mpa.builder().id(1L).name("G").build())
                .genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
                .build());
        Film film = filmDaoImpl.createFilm(Film.builder()
                .name("labore23")
                .description("Duis")
                .releaseDate(LocalDate.of(1979, 4, 17))
                .duration(150)
                .rate(4)
                .mpa(Mpa.builder().id(1L).name("G").build())
                .genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
                .build());
        genreDaoImpl.updateFilmGenres(film);

        likeDaoImpl.addLikeToFilm(1L, 1L);
        likeDaoImpl.addLikeToFilm(2L, 1L);
        likeDaoImpl.addLikeToFilm(2L, 2L);

		/*assertThat(likeDaoImpl.findPopularFilms(1).size())
				.isEqualTo(1);*/

    }

    @Test
    public void testGetAllGenres() {
        assertThat(genreDaoImpl.findAllGenre().size())
                .isEqualTo(6);
    }

    @Test
    public void testGetAllMpa() {
        assertThat(mpaDaoImpl.findAllMpa().size())
                .isEqualTo(5);
    }

    @Test
    public void testCreateUpdateAndGetDirector() {
        Director director = Director.builder()
                .id(1L)
                .name("test")
                .build();
        directorDaoImpl.createDirector(director);
        assertEquals(directorDaoImpl.getDirectorById(1l), director);
        director.setName("newName");
        directorDaoImpl.updateDirector(director);
        assertEquals(directorDaoImpl.getDirectorById(1l).getName(), "newName");
    }

    @Test
    public void testDeleteAndGetAllDirectors() {
        Director director = Director.builder()
                .id(1L)
                .name("test")
                .build();
        directorDaoImpl.createDirector(director);
        assertEquals(directorDaoImpl.getAll().size(), 2);
        directorDaoImpl.deleteDirector(2l);
        assertEquals(directorDaoImpl.getAll().size(), 1);
    }

    @Test
    public void testCommonFilms() {
        assertEquals(filmDaoImpl.getCommonFilms(3l, 4l).size(), 0);
        likeDaoImpl.addLikeToFilm(3l, 3l);
        likeDaoImpl.addLikeToFilm(3l, 4l);
        likeDaoImpl.addLikeToFilm(1l, 3l);
        likeDaoImpl.addLikeToFilm(1l, 4l);
        System.out.println(filmDaoImpl.getCommonFilms(3l, 4l));
        assertEquals(filmDaoImpl.getCommonFilms(3l, 4l).size(), 2);
        assertEquals(filmDaoImpl.getCommonFilms(3l, 4l).get(0).getId(), 1);
    }
}
