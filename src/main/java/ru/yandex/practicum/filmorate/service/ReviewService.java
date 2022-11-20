package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewDao reviewDao;

    public Review createReview(Review review) {
        return reviewDao.createReview(review);
    }

    public Review updateReview(Review review) {
        return reviewDao.updateReview(review);
    }

    public boolean removeReview(long reviewId) {
        return reviewDao.deleteReview(reviewId);
    }

    public Review getReviewById(long reviewId) {
        return reviewDao.getReviewById(reviewId);
    }

    public List<Review> getAllReviews() {
        return reviewDao.getAllReviews();
    }

    public List<Review> getReviewByFilmId(long filmId, Optional<Integer> count) {
        return reviewDao.getReviewByFilmId(filmId, count);
    }
}
