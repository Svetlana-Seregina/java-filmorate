package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class Event {
    private Long eventId;
    @Positive
    private Long userId;
    @Positive
    private Long entityId;
    @NotNull
    private String eventType;
    @NotNull
    private String operation;
    @NotBlank
    private Long timestamp;
}
