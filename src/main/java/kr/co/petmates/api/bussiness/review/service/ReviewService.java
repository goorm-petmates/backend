package kr.co.petmates.api.bussiness.review.service;

import kr.co.petmates.api.bussiness.review.entity.Review;
import kr.co.petmates.api.bussiness.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class ReviewService {
    private final ReviewRepository reviewRepository;


    public Review createReview(int rating, String contents) {
        Review review = new Review();

        review.setContents(contents);
        review.setRating(rating);
        return reviewRepository.save(review);
    }
}
