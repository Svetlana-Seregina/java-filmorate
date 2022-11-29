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
        log.info("F8-1. Создание отзыва");
        userService.findUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        Review createdReview = reviewDao.createReview(review);
        eventFeedService.addReviewEvent(createdReview.getUserId(), createdReview.getReviewId());
        return createdReview;
    }

    public Review updateReview(Review review) {
        log.info("F8-2. Обновление отзыва");
        Review updatedReview = reviewDao.updateReview(review);
        eventFeedService.updateReviewEvent(updatedReview.getUserId(), updatedReview.getReviewId());
        return updatedReview;
    }

    public boolean removeReview(long reviewId) {
        log.info("F8-3. Удаление отзыва");
        Long userId = getReviewById(reviewId).getUserId();
        boolean isRemoveReview = reviewDao.deleteReview(reviewId);
        if (isRemoveReview) {
            eventFeedService.removeReviewEvent(userId, reviewId);
        }
        return isRemoveReview;
    }

    public Review getReviewById(long reviewId) {
        log.info("F8-5. Получение отзыва по id");
        return reviewDao.getReviewById(reviewId);
    }

    public List<Review> getAllReviews() {
        log.info("F8-4. Получение всех отзывов");
        return reviewDao.getAllReviews();
    }

    public List<Review> getReviewByFilmId(long filmId, Optional<Integer> count) {
        log.info("F8-5. Получение отзывов фильма");
        return reviewDao.getReviewsByFilmId(filmId, count);
    }

    public boolean addLikeToReview(long reviewId, long userId) {
        log.info("F9-1. Добавление лайка отзыву");
        return reviewLikeDao.addLikeToReview(reviewId, userId);
    }

    public boolean addDislikeToReview(long reviewId, long userId) {
        log.info("F9-2. Добавление дизлайка отзыву");
        return reviewLikeDao.addDislikeToReview(reviewId, userId);
    }

    public boolean removeLikeDislikeFromReview(long reviewId, long userId) {
        log.info("F9-3. Удаление лайка/дизлайка у отзыва");
        return reviewLikeDao.removeLikeDislikeFromReview(reviewId, userId);
    }
}
