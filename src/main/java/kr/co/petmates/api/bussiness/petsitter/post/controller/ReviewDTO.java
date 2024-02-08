package kr.co.petmates.api.bussiness.petsitter.post.controller;

import kr.co.petmates.api.bussiness.review.entity.Review;

public class ReviewDTO {

    private Long id;
    private Long memberId;
    private Long petsitterId;
    private Integer rating;
    private String contents;

    public ReviewDTO() {
    }

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.memberId = review.getMembers().getId();
        this.petsitterId = review.getPost().getId();
        this.rating = review.getRating();
        this.contents = review.getContents();
    }
}
