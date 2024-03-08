package kr.co.petmates.api.bussiness.review.controller;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.petmates.api.bussiness.review.dto.ReviewDto;
import kr.co.petmates.api.bussiness.review.entity.Review;
import kr.co.petmates.api.bussiness.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/my-page/review")
    public ResponseEntity<Review> createReview(@RequestBody ReviewDto reviewDto) {
        Review review = reviewService.createReview(reviewDto.getRating(), reviewDto.getContents());
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

//    @GetMapping("/reviews")
    @GetMapping("/petsitter/reviews")
    public List<ReviewDto> viewReview(@RequestParam Long memberId) { //ResponseEntity<?>
//        try {
//            List<Review> reviews = reviewService.getReviewsByMemberId(memberId);
//            if (reviews != null) {
//                Map<String, Object> successRes = new HashMap<>();
//                successRes.put("result", "success");
//                successRes.put("data", reviews);
//
//                return ResponseEntity.ok(successRes);
//            } else {
//                Map<String, Object> errorRes = new HashMap<>();
//                errorRes.put("result", "fail");
//                errorRes.put("data", Collections.singletonMap("reason", "리뷰가 존재하지 않습니다."));
//
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRes);
//            }
//        } catch (Exception e) {
//            Map<String, Object> errorRes = new HashMap<>();
//            errorRes.put("result", "fail");
//            errorRes.put("data", Collections.singletonMap("reason", "에러가 발생했습니다."));
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
//        }

        List<Review> reviews = reviewService.getReviewsByMemberId(memberId);

        return reviews.stream().map(ReviewDto::toReviewDto).collect(Collectors.toList());
    }
}
