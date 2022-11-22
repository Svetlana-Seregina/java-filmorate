package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.ReviewLikeDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewLikeDao reviewLikeDao;
    private final UserService userService;
    private final FilmService filmService;

    @Override
    public Review createReview(Review review) {
        //делаю поиск пользователя или фильма, если их нет, то соответствующие методы выбросят исключение
        userService.findUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());

        String sqlQuery = "INSERT INTO reviews (content, user_id, film_id, is_positive)" +
                " values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setLong(2, review.getUserId());
            stmt.setLong(3, review.getFilmId());
            stmt.setBoolean(4, review.getIsPositive());
            return stmt;
        }, keyHolder);

        long reviewId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        review.setReviewId(reviewId);
        review.setUseful(0L);
        return review;
    }

    @Override
    public Review updateReview(Review review) {

        String sqlQuery = "UPDATE reviews SET " +
                "content = ?, is_positive = ? WHERE review_id = ?";

        int updatedRows = jdbcTemplate.update(sqlQuery
                , review.getContent()
                , review.getIsPositive()
                , review.getReviewId());

        if (updatedRows == 0) {
            throw new EntityNotFoundException("Отзыв не найден, review id = " + review.getReviewId());
        }
        return review;
    }

    @Override
    public boolean deleteReview(long reviewId) {
        String sqlQuery = "DELETE FROM reviews WHERE review_id = ?";
        return jdbcTemplate.update(sqlQuery, reviewId) > 0;
    }

    @Override
    public List<Review> getAllReviews() {
        String sqlQuery = "SELECT * FROM reviews";
        List<Review> res = jdbcTemplate.query(sqlQuery, this::mapRowToReview);
        res.sort((t1, t2) -> (int) (t2.getUseful() - t1.getUseful()));
        return res;
    }

    @Override
    public Review getReviewById(long reviewId) {
        String sqlFilmRow = "SELECT * FROM reviews WHERE review_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlFilmRow, this::mapRowToReview, reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Отзыв с review_id=%d не найден", reviewId));
        }
    }

    @Override
    public List<Review> getReviewsByFilmId(long filmId, Optional<Integer> count) {
        String sqlFilmRow;

        if (count.isPresent()) {
            sqlFilmRow = "SELECT * FROM reviews WHERE film_id = ? LIMIT ?";
        } else {
            sqlFilmRow = "SELECT * FROM reviews WHERE film_id = ?";
        }
        try {
            List<Review> reviews;
            if (count.isPresent()) {
                reviews = jdbcTemplate.query(sqlFilmRow, this::mapRowToReview, filmId, count.get());
            } else {
                reviews = jdbcTemplate.query(sqlFilmRow, this::mapRowToReview, filmId);
            }
            reviews.sort((t1, t2) -> (int) (t2.getUseful() - t1.getUseful()));
            return reviews;
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Отзыв для фильма filmd_id=%d не найден", filmId));
        }
    }

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                     .reviewId(resultSet.getLong("review_id"))
                     .content(resultSet.getString("content"))
                     .userId(resultSet.getLong("user_id"))
                     .filmId(resultSet.getLong("film_id"))
                     .isPositive(resultSet.getBoolean("is_positive"))
                     .useful(reviewLikeDao.calculateRating(resultSet.getLong("review_id")))
                     .build();
    }
}
