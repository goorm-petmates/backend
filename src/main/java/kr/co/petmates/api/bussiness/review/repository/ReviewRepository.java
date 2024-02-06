package kr.co.petmates.api.bussiness.review.repository;

import kr.co.petmates.api.bussiness.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
