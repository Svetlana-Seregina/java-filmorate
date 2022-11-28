package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewLikeDao;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewLikeDaoImpl implements ReviewLikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLikeToReview(Long reviewId, Long userId) {
        return jdbcTemplate.update(insertReviewLikes(true), reviewId, userId) > 0;
    }

    @Override
    public boolean addDislikeToReview(Long reviewId, Long userId) {
        return jdbcTemplate.update(insertReviewLikes(false), reviewId, userId) > 0;
    }

    @Override
    public boolean removeLikeDislikeFromReview(Long reviewId, Long userId) {
        String sqlQuery = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";
        return jdbcTemplate.update(sqlQuery, reviewId, userId) > 0;
    }

    private String insertReviewLikes(boolean b) {
        return "INSERT INTO review_likes (review_id, user_id, islike) VALUES (?, ?, " + b + ")";
    }
}
