package ru.yandex.practicum.filmorate.dao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    Review createReview(Review review);

    Review updateReview(Review review);

    boolean deleteReview(long reviewId);

    List<Review> getAllReviews();

    Review getReviewById(long reviewId);

    List<Review> getReviewByFilmId(long filmId, Optional<Integer> count);
}
