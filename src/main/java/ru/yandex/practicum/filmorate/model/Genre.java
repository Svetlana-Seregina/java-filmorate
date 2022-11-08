package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

// Таблицы соответствуют классам, колонки — полям, строки — объектам
// Именно таким соответствием представлены базовые правила маппинга.
@Data
@Builder
public class Genre {

    private final Long id;
    private final String name;


    //COMEDY,
    //DRAMA,
    //CARTOON,
    //THRILLER,
    //DOCUMENTARY,
    //ACTION_MOVIE
}
