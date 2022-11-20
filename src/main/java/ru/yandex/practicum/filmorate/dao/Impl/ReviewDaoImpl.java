package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.ReviewLikeDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

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

    @Override
    public Review createReview(Review review) {
        try {
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
        } catch (DataIntegrityViolationException e) {
            log.warn("Пользователь или фильм не найден в базе");
            throw new EntityNotFoundException("Пользователь или фильм не найден в базе");
        }
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
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview);
    }

    @Override
    public Review getReviewById(long reviewId) {
        String sqlFilmRow = "SELECT * FROM reviews WHERE review_id = ?";
        Review review;

        try {
            review = jdbcTemplate.queryForObject(sqlFilmRow, this::mapRowToReview, reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Отзыв с review_id=%d не найден", reviewId));
        }
        return review;
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
