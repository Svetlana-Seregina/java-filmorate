package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewLikeDao;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewLikeDaoImpl implements ReviewLikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLikeToReview(Long reviewId, Long userId) {
        return false;
    }

    @Override
    public boolean addDislikeToReview(Long reviewId, Long userId) {
        return false;
    }

    @Override
    public boolean removeLikeFromReview(Long reviewId, Long userId) {
        return false;
    }

    @Override
    public boolean removeDislikeFromReview(Long reviewId, Long userId) {
        return false;
    }

    @Override
    public Long calculateRating(Long reviewId) {
        String sqlQuery = "SELECT SUM (CASE WHEN islike = true THEN 1 " +
                "WHEN islike = false THEN 0 END) as rating " +
                "FROM review_likes WHERE review_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> rs.getLong("rating"), reviewId);
    }
}
