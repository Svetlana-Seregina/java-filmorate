package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;

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

    public User(Long id, String name, String email, String login, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }


}

