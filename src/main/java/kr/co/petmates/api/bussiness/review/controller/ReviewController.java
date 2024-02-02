package kr.co.petmates.api.bussiness.review.controller;

import kr.co.petmates.api.bussiness.review.dto.ReviewDto;
import kr.co.petmates.api.bussiness.review.entity.Review;
import kr.co.petmates.api.bussiness.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my-page/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewDto reviewDto) {
        Review review = reviewService.createReview(reviewDto.getRating(), reviewDto.getContents());
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }
}
