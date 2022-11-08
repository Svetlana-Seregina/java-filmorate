package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class Film{
    private Long id;
    @NotBlank // пресекает передачу null либо же название только из пробелов
    private final String name;
    @Size(max = 200)
    private final String description;
    @NotNull // запрет на передачу пустого поля
    private final LocalDate releaseDate;
    @Positive // запрет на передачу отрицательного значения
    @Min(1)
    private final int duration;
    private final Integer rate;

    private final Set<Long> likes = new TreeSet<>(); // множество IdUsers, условие «один пользователь — один лайк» для оценки фильмов

    // MPA рейтинг фильма.
    // Рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА)
    private final Mpa mpa;
    private List<Genre> genres;

}
