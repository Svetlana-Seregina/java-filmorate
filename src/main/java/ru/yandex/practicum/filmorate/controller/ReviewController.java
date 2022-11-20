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
        log.info("Получен запрос на создание отзыва");
        Review reviewCreated = reviewService.createReview(review);
        log.info("Создан отзыв ID = " + reviewCreated.getReviewId());
        return reviewCreated;
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{reviewId}")
    public boolean removeReview(@Valid @PathVariable long reviewId) {
        return reviewService.removeReview(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReviewById(@Valid @PathVariable long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping()
    public List<Review> getReviewByFilmId(@RequestParam Optional<Long> filmId, @RequestParam Optional<Integer> count) {
        if (filmId.isPresent()) {
            return reviewService.getReviewByFilmId(filmId.get(), count);
        } else {
            return reviewService.getAllReviews();
        }
    }
}
