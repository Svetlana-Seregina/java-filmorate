package ru.yandex.practicum.filmorate.controller;

public class FilmControllerTest {

    //private final FilmController filmController;

    /*@Test
    void createAndDeleteFilm() {
        Film film = new Film ("Cramer vs Cramer", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), 105);
        filmController.saveFilm(film);
        assertEquals(105, film.getDuration());
        filmController.deleteFilm(1L);
        assertEquals(0, filmController.findAll().size());*/
    }

   /*@Test
    void findAll() {
        Film film = new Film("Cramer vs Cramer", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), 105);
        assertEquals(film, filmController.create(film));
        assertEquals(1, filmController.findAll().size());
    }*/

   /*@Test
    void findAllEmptySet() {
        assertEquals(0, filmController.findAll().size());
    }*/

    /*@Test
    void getFilmById() {
        Film film = new Film("Cramer vs Cramer", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), 105);
        filmController.create(film);
        assertEquals(1L, filmController.findFilm(1L).getId());
    }*/
   /*@Test
    void createFilmWithWrongReleaseDate() {
        Film film = new Film(null, "There are three sides to this love story!",
                LocalDate.of(1879, 12, 7), 105);
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }*/

    /*@Test
    void createFilmWithNegativeDuration() {
        Film film = new Film(null, "There are three sides to this love story!",
                LocalDate.of(1879, 12, 7), -105);
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }*/

    /*@Test
    void findTenPopularFilms() {
        Film film = new Film("Cramer vs Cramer", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), 105);
        filmController.create(film);
        assertEquals(1, filmController.findTenPopularFilms(2).size());
    }*/

   /* @Test
    void addAndDeleteLike() {
        Film film = new Film("Cramer vs Cramer", "There are three sides to this love story!",
                LocalDate.of(1979, 12, 7), 105);
        filmController.create(film);
        User user = User.builder()
                .id(1L)
                .name("Maria")
                .email("maria0@yandex.ru")
                .login("masha235")
                .birthday(LocalDate.of(2000, 11, 15))
                .build();
        assertThrows(EntityNotFoundException.class, () -> filmController.addLikeToFilm(1L, user.getId()));
    }*/

//}