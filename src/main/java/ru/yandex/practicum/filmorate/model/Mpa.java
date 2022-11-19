package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mpa {

    private final Long id;
    private final String name;


    // G,      // у фильма нет возрастных ограничений
    // PG,     // детям рекомендуется смотреть фильм с родителями
    // PG_13,  // детям до 13 лет просмотр не желателен
    // R,      // лицам до 17 лет просматривать фильм можно только в присутствии взрослого
    // NC_17   // лицам до 18 лет просмотр запрещён
}
