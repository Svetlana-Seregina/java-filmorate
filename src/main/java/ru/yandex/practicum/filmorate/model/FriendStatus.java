package ru.yandex.practicum.filmorate.model;

public enum FriendStatus {
    UNCONFIRMED,    // неподтверждённая — пользователь отправил запрос на добавление другого пользователя в друзья
    CONFIRMED       // подтверждённая — второй пользователь согласился на добавление
}
