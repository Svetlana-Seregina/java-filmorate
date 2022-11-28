package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_REVIEW_BY_ID = "SELECT r.review_id, r.content, r.user_id, r.film_id, " +
            "r.is_positive, " +
            "SUM (CASE WHEN rl.islike = true THEN 1 " +
            "WHEN rl.islike = false THEN -1 " +
            "WHEN rl.islike IS NULL THEN 0 END) AS rating FROM reviews r " +
            "LEFT JOIN review_likes rl ON r.review_id  = rl.review_id " +
            "WHERE r.review_id = ? " +
            "GROUP BY r.review_id " +
            "ORDER BY rating DESC";

    @Override
    public Review createReview(Review review) {
        String sqlQuery = "INSERT INTO reviews (content, user_id, film_id, is_positive) VALUES (?, ?, ?, ?)";
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
        String sqlQuery = "UPDATE reviews SET content = ?, is_positive = ? WHERE review_id = ?";
        long reviewId = review.getReviewId();
        int updatedRows = jdbcTemplate.update(sqlQuery
                , review.getContent()
                , review.getIsPositive()
                , reviewId);

        if (updatedRows == 0) {
            throw new EntityNotFoundException(String.format("Отзыв с id=%d не найден", reviewId));
        }
        return getReviewById(reviewId);
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        String sqlQuery = "DELETE FROM reviews WHERE review_id = ?";
        return jdbcTemplate.update(sqlQuery, reviewId) > 0;
    }

    @Override
    public List<Review> getAllReviews() {
        String sqlQuery = SELECT_REVIEW_BY_ID.replace("WHERE r.review_id = ? ", "");
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview);
    }

    @Override
    public Review getReviewById(Long reviewId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_REVIEW_BY_ID, this::mapRowToReview, reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Отзыв с id=%d не найден", reviewId));
        }
    }

    @Override
    public List<Review> getReviewsByFilmId(Long filmId, Optional<Integer> count) {
        String sqlFilmRow = SELECT_REVIEW_BY_ID.replace("r.review_id = ?", "r.film_id = ?");
        if (count.isPresent()) {
            sqlFilmRow = sqlFilmRow + " LIMIT ?";
        }
        try {
            if (count.isPresent()) {
                return jdbcTemplate.query(sqlFilmRow, this::mapRowToReview, filmId, count.get());
            } else {
                return jdbcTemplate.query(sqlFilmRow, this::mapRowToReview, filmId);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Отзыв для фильма c id=%d не найден", filmId));
        }
    }

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                     .reviewId(resultSet.getLong("review_id"))
                     .content(resultSet.getString("content"))
                     .userId(resultSet.getLong("user_id"))
                     .filmId(resultSet.getLong("film_id"))
                     .isPositive(resultSet.getBoolean("is_positive"))
                     .useful(resultSet.getLong("rating"))
                     .build();
    }
}
