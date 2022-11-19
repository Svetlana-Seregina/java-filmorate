package ru.yandex.practicum.filmorate.dao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewDao {
    Review createReview(Review review);

    Review updateReview(Review review);

    boolean deleteReview(long reviewId);

    List<Review> getAllReviews();

    Review getReviewById(long reviewId);
}
