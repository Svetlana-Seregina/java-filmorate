package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force=true)
public class User {
    private Integer id;
    private String name;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    @NotNull
    private final String login;
    private final LocalDate birthday;


    public void setName(String name) {
        this.name = name;
    }


}

