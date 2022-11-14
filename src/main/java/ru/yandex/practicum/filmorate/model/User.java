package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
public class User {
    private Long id;
    private String name;
    @NotNull
    @Email
    private final String email;
    @NotBlank
    private final String login;
    @NotNull
    @PastOrPresent
    private final LocalDate birthday;

    public User(Long id, String name, String email, String login, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

}

