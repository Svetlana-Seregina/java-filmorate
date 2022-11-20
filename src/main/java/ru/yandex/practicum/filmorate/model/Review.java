package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;

@Data
@Builder
public class Review {
    private long reviewId;
    @NotNull
    private String content;
    @NotNull
    @Positive
    private Long userId;
    @NotNull
    @Positive
    private Long filmId;
    @NotNull
    private Boolean isPositive;
    private Long useful;
}
