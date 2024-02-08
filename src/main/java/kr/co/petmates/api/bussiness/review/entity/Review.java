package kr.co.petmates.api.bussiness.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.petsitter.post.entity.Post;
import kr.co.petmates.api.bussiness.review.dto.ReviewDto;
import kr.co.petmates.api.common.entity.BaseDateTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "REVIEW")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Getter
@Setter
@ToString

public class Review extends BaseDateTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private int rating; // 별점

//    @Column(nullable = false)
    private String contents; // 내용

    public static Review toReviewEntity(ReviewDto reviewDto) { // dto -> entity
        Review review = new Review();
        review.setRating(reviewDto.getRating());
        review.setContents(reviewDto.getContents());

        return review;
    }
    // 등록자와 연결
    @Getter
    @ManyToOne
    @JoinColumn(name = "membersId")
    private Members members;
    public void setMembers(Members members) {
        this.members = members;
    }

    // 펫시터와 연결 (펫시터 엔티티 생성 후..)
    @Getter
    @ManyToOne
    @JoinColumn(name = "petsitter")
    private Post post;
    public void setPost(Post post) {
        this.post = post;
    }
}
