package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class Film {
    private Integer id;
    @NotBlank // пресекает передачу null либо же название только из пробелов
    private final String name;
    @Size(max = 200)
    private final String description;
    @NotNull // запрет на передачу пустого поля
    private final LocalDate releaseDate;
    // @Positive // запрет на передачу отрицательного значения
    @Min(1)
    private final int duration;

    public void setId(Integer id) {
        this.id = id;
    }


}
