package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

@Data
@Builder
public class User {
    private Long id;
    private String name;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    private final String login;
    @NotNull
    @PastOrPresent
    private final LocalDate birthday;
    private final Set<Long> friends = new TreeSet<>(); // Set<Long> c id друзей
    private final FriendStatus friendStatus;


}

