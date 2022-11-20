package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface ReviewLikeDao {
    boolean addLikeToReview(Long reviewId, Long userId);

    boolean addDislikeToReview(Long reviewId, Long userId);

    boolean removeLikeFromReview(Long reviewId, Long userId);

    boolean removeDislikeFromReview(Long reviewId, Long userId);

    Long calculateRating(Long reviewId);
}
