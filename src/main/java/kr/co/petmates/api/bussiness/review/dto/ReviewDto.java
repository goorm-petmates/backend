package kr.co.petmates.api.bussiness.review.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import kr.co.petmates.api.bussiness.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

    private Long id; // 시퀀스

    @NotNull
//    @Pattern
    private int rating; // 별점

//    @NotNull
    private String contents; // 내용

    public static ReviewDto toReviewDto(Review review) { // entity -> dto
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setRating(review.getRating());
        reviewDto.setContents(review.getContents());

        return reviewDto;
    }

    // 파일 URL 필드
    private String photoUrl;
}

