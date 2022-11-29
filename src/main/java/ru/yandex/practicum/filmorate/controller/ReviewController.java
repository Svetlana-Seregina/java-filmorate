package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/reviews")
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        log.info("Обрабатываем запрос на создание отзыва");
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("Обрабатываем запрос на обновление отзыва с id = " + review.getReviewId());
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{reviewId}")
    public boolean removeReview(@Valid @PathVariable Long reviewId) {
        log.info("Обрабатываем запрос на удаление отзыва с id = " + reviewId);
        return reviewService.removeReview(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReviewById(@Valid @PathVariable Long reviewId) {
        log.info("Обрабатываем запрос на получение отзыва с id = " + reviewId);
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping()
    public List<Review> getAllReviewsOrByFilmId(@RequestParam Optional<Long> filmId, @RequestParam Optional<Integer> count) {
        if (filmId.isPresent()) {
            log.info("Обрабатываем запрос на получение списка отзывов для фильма с id = " + filmId.get());
            return reviewService.getReviewByFilmId(filmId.get(), count);
        } else {
            log.info("Запрошен список отзывов для фильмов. Параметр \"ID фильма\" не задан");
            return reviewService.getAllReviews();
        }
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public boolean addLikeToReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        log.info("Обрабатываем запрос на добавление лайка для отзыва с id = {} от пользователя с id = {}",
                reviewId, userId);
        return reviewService.addLikeToReview(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public boolean addDislikeToReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        log.info("Обрабатываем запрос на добавление дизлайка для отзыва с id = {} от пользователя с id = {}",
                reviewId, userId);
        return reviewService.addDislikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public boolean removeLikeToReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        log.info("Обрабатываем запрос на удаление лайка для отзыва с id = {} от пользователя с id = {}",
                reviewId, userId);
        return reviewService.removeLikeDislikeFromReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public boolean removeDislikeToReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        log.info("Обрабатываем запрос на удаление дизлайка для отзыва с id = {} от пользователя с id = %{}",
                reviewId, userId);
        return reviewService.removeLikeDislikeFromReview(reviewId, userId);
    }
}
