DROP TABLE IF EXISTS users, mpa, directors, films, friend, likes, genres, film_genre, reviews, review_likes, feed;


CREATE TABLE IF NOT EXISTS users
(
    user_id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     varchar(255) NOT NULL,
    email    varchar(255) NOT NULL,
    login    varchar(255) NOT NULL,
    birthday datetime,
    CONSTRAINT users_pk PRIMARY KEY (user_id)
);

CREATE UNIQUE INDEX IF NOT EXISTS USER_EMAIL_UINDEX on USERS (email);
CREATE UNIQUE INDEX IF NOT EXISTS USER_LOGIN_UINDEX on USERS (login);


CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name   varchar(40),
    CONSTRAINT mpa_pk PRIMARY KEY (mpa_id)
);


CREATE TABLE IF NOT EXISTS directors
(
    director_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR NOT NULL
);


CREATE TABLE IF NOT EXISTS films
(
    film_id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         varchar(255) NOT NULL,
    description  varchar(255) NOT NULL,
    release_date date,
    duration     BIGINT,
    mpa_id       BIGINT,
    rate         BIGINT,
    CONSTRAINT films_pk PRIMARY KEY (film_id),
    CONSTRAINT fk_mpa_id FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id),
    director_id  BIGINT REFERENCES directors (director_id)
);

CREATE TABLE IF NOT EXISTS friend
(
    user_id   BIGINT REFERENCES users (user_id),
    friend_id BIGINT REFERENCES users (user_id),
    CONSTRAINT friends_pk PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes
(
    user_id BIGINT REFERENCES users (user_id),
    film_id BIGINT REFERENCES films (film_id),
    CONSTRAINT likes_pk PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     VARCHAR(40),
    CONSTRAINT genres_pk PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  BIGINT REFERENCES films (film_id),
    genre_id BIGINT REFERENCES genres (genre_id),
    CONSTRAINT film_genre_pk PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS reviews
(
    review_id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id     BIGINT  NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    film_id     BIGINT  NOT NULL REFERENCES films (film_id) ON DELETE CASCADE,
    content     VARCHAR NOT NULL,
    is_positive BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS review_likes
(
    user_id   BIGINT  NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    review_id BIGINT  NOT NULL REFERENCES reviews (review_id) ON DELETE CASCADE,
    isLike    BOOLEAN NOT NULL,
    PRIMARY KEY (user_id, review_id)
);

CREATE TABLE IF NOT EXISTS feed
(
    event_id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id    BIGINT    NOT NULL REFERENCES users (user_id),
    entity_id  BIGINT    NOT NULL,
    event_type VARCHAR   NOT NULL,
    operation  VARCHAR   NOT NULL,
    timestamp  TIMESTAMP NOT NULL
);