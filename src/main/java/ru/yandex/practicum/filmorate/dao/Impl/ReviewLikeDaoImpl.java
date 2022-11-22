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
        String sqlQuery = "INSERT INTO review_likes (review_id, user_id, islike)" +
                " values (?, ?, true)";
        return jdbcTemplate.update(sqlQuery, reviewId, userId) > 0;
    }

    @Override
    public boolean addDislikeToReview(Long reviewId, Long userId) {
        String sqlQuery = "INSERT INTO review_likes (review_id, user_id, islike)" +
                " values (?, ?, false)";
        return jdbcTemplate.update(sqlQuery, reviewId, userId) > 0;
    }

    @Override
    public boolean removeLikeDislikeFromReview(Long reviewId, Long userId) {
        String sqlQuery = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";
        return jdbcTemplate.update(sqlQuery, reviewId, userId) > 0;
    }

    @Override
    public Long calculateRating(Long reviewId) {
        String sqlQuery = "SELECT SUM (CASE WHEN islike = true THEN 1 " +
                "WHEN islike = false THEN -1 END) as rating " +
                "FROM review_likes WHERE review_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> rs.getLong("rating"), reviewId);
    }
}
