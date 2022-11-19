package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friends {

    private final Long userId;
    private final Long friendId;

    public Friends(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

}
