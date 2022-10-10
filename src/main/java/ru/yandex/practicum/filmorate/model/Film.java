package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class Film{
    private Long id;
    @NotBlank // пресекает передачу null либо же название только из пробелов
    private final String name;
    @Size(max = 200)
    private final String description;
    @NotNull // запрет на передачу пустого поля
    private final LocalDate releaseDate;
    // @Positive // запрет на передачу отрицательного значения
    @Min(1)
    private final int duration;

    private final Set<Long> likes = new TreeSet<>(); // множество IdUsers, условие «один пользователь — один лайк» для оценки фильмов

    public void setId(Long id) {
        this.id = id;
    }

}
