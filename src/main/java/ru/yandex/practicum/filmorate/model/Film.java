package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Film {
    private Integer id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;

    public void setId(Integer id) {
        this.id = id;
    }


}
