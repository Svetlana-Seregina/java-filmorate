package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Director {

    private Long id;

    @NotBlank
    @NotNull
    private final String name;

}
