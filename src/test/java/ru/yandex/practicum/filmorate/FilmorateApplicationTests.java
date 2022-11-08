package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.Impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.dao.Impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.dao.Impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class FilmorateApplicationTests {
	private final UserDbStorage userDbStorage;
	private final FilmDbStorage filmDbStorage;
	private final GenreDaoImpl genreDaoImpl;
	private final MpaDaoImpl mpaDaoImpl;

	@Test
	void contextLoads() {
	}
	@Test
	public void testFindUserById() {
		User user = userDbStorage.createUser(User.builder()
				.name("Nick Name")
				.email("mail@mail.ru")
				.login("dolore")
				.birthday(LocalDate.of(2000, 11, 15))
				.build());

		assertThat(userDbStorage.findUserById(user.getId()))
				.isNotNull()
				.isEqualTo(user);
	}


	@Test
	public void testUpdateUser() {
		User user2 = userDbStorage.createUser(User.builder()
				.name("Nick Name5")
				.email("mail5@mail.ru")
				.login("dolore5")
				.birthday(LocalDate.of(2000, 11, 15))
				.build());
		log.info("Создан пользователь user с name = {}", user2.getName());
		user2 = userDbStorage.updateUser(User.builder()
				.id(2L)
				.name("est adipisicing")
				.email("mail@yandex.ru")
				.login("doloreUpdate")
				.birthday(LocalDate.of(2000, 11, 15))
				.build());
		log.info("Пользователь user поменял имя на name = {}", user2.getName());
		assertThat(userDbStorage.findUserById(user2.getId()).getName()).isEqualTo("est adipisicing");

	}

	@Test
	public void testFindAllUsers() {
		userDbStorage.createUser(User.builder()
				.name("Nick Name2")
				.email("mail2@mail.ru")
				.login("dolore2")
				.birthday(LocalDate.of(2000, 11, 15))
				.build());
		userDbStorage.createUser(User.builder()
				.name("Nick Name3")
				.email("mail3@mail.ru")
				.login("dolore3")
				.birthday(LocalDate.of(2000, 11, 15))
				.build());
		assertThat(userDbStorage.findAllUsers().size())
				.isEqualTo(2);
	}

	@Test
	public void testUpdateFilmById() {
		Film film = filmDbStorage.createFilm(Film.builder()
				.name("labore nulla")
				.description("Duis in consequat esse")
				.releaseDate(LocalDate.of(1979, 4, 17))
				.duration(140)
				.rate(4)
				.mpa(Mpa.builder().id(1L).name("G").build())
				.genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
				.build());

		filmDbStorage.updateFilm(Film.builder()
				.id(1L)
				.name("Film Updated")
				.description("Duis in consequat esse")
				.releaseDate(LocalDate.of(1979, 4, 17))
				.duration(150)
				.rate(4)
				.mpa(Mpa.builder().id(1L).name("G").build())
				.genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
				.build());

		assertThat(filmDbStorage.getFilmById(film.getId()))
				.hasFieldOrPropertyWithValue("name", "Film Updated");
	}

	@Test
	public void testGetFilmById() {
		Film film = filmDbStorage.createFilm(Film.builder()
				.name("labore nulla4")
				.description("Duis in consequat esse4")
				.releaseDate(LocalDate.of(1979, 4, 17))
				.duration(140)
				.rate(4)
				.mpa(Mpa.builder().id(1L).name("G").build())
				.genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
				.build());

		assertThat(filmDbStorage.getFilmById(film.getId()))
				.isNotNull()
				.isEqualTo(film);
	}

	@Test
	public void testFilmAddLike() {
		userDbStorage.createUser(User.builder()
				.name("Nick Name4")
				.email("mail4@mail.ru")
				.login("dolore4")
				.birthday(LocalDate.of(2000, 11, 15))
				.build());
		userDbStorage.createUser(User.builder()
				.name("Nick")
				.email("mail45@mail.ru")
				.login("dolores")
				.birthday(LocalDate.of(2000, 10, 15))
				.build());

		filmDbStorage.createFilm(Film.builder()
				.name("labore nulla")
				.description("Duis in consequat esse")
				.releaseDate(LocalDate.of(1979, 4, 17))
				.duration(140)
				.rate(4)
				.mpa(Mpa.builder().id(1L).name("G").build())
				.genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
				.build());
		filmDbStorage.createFilm(Film.builder()
				.name("labore23")
				.description("Duis")
				.releaseDate(LocalDate.of(1979, 4, 17))
				.duration(150)
				.rate(4)
				.mpa(Mpa.builder().id(1L).name("G").build())
				.genres(List.of(Genre.builder().id(1L).name("Комедия").build()))
				.build());

		filmDbStorage.addLikeToFilm(1L, 1L);
		filmDbStorage.addLikeToFilm(2L, 1L);
		filmDbStorage.addLikeToFilm(2L, 2L);


		assertThat(filmDbStorage.findPopularFilms(1).size())
				.isEqualTo(1);

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

}
