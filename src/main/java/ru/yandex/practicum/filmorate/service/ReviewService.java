package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.ReviewLikeDao;
import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewDao reviewDao;
    private final ReviewLikeDao reviewLikeDao;
    private final UserService userService;
    private final FilmService filmService;
    private final EventFeedService eventFeedService;

    public Review createReview(Review review) {
        //делаю поиск пользователя или фильма, если их нет, то соответствующие методы выбросят исключение
        userService.findUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        Review createdReview = reviewDao.createReview(review);
        eventFeedService.addReviewEvent(createdReview.getUserId(), createdReview.getReviewId());
        return createdReview;
    }

    public Review updateReview(Review review) {
        Review updatedReview = reviewDao.updateReview(review);
        eventFeedService.updateReviewEvent(updatedReview.getUserId(), updatedReview.getReviewId());
        return updatedReview;
    }

    public boolean removeReview(long reviewId) {
        Long userId = getReviewById(reviewId).getUserId();
        boolean isRemoveReview = reviewDao.deleteReview(reviewId);
        if (isRemoveReview) {
            eventFeedService.removeReviewEvent(userId, reviewId);
        }
        return isRemoveReview;
    }

    public Review getReviewById(long reviewId) {
        return reviewDao.getReviewById(reviewId);
    }

    public List<Review> getAllReviews() {
        return reviewDao.getAllReviews();
    }

    public List<Review> getReviewByFilmId(long filmId, Optional<Integer> count) {
        return reviewDao.getReviewsByFilmId(filmId, count);
    }

    public boolean addLikeToReview(long reviewId, long userId) {
        return reviewLikeDao.addLikeToReview(reviewId, userId);
    }

    public boolean addDislikeToReview(long reviewId, long userId) {
        return reviewLikeDao.addDislikeToReview(reviewId, userId);
    }

    public boolean removeLikeDislikeFromReview(long reviewId, long userId ) {
        return reviewLikeDao.removeLikeDislikeFromReview(reviewId, userId);
    }
}
