package ru.yandex.practicum.filmorate.dao;

public interface ReviewLikeDao {
    boolean addLikeToReview(Long reviewId, Long userId);

    boolean addDislikeToReview(Long reviewId, Long userId);

    boolean removeLikeDislikeFromReview(Long reviewId, Long userId);
}
