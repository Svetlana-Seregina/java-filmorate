package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.List;

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
    @NotBlank
    private final String description;
    @NotNull // запрет на передачу пустого поля
    private final LocalDate releaseDate;
    @Positive // запрет на передачу отрицательного значения
    private final int duration;
    private Integer rate;

    // MPA рейтинг фильма.
    // Рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА)
    @NotNull
    private final Mpa mpa;
    private List<Genre> genres;
    private List<Director> directors;
}
