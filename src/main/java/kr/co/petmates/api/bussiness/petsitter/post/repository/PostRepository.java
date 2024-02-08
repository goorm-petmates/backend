package kr.co.petmates.api.bussiness.petsitter.post.repository;

import java.util.List;
import java.util.Optional;
import kr.co.petmates.api.bussiness.petsitter.post.entity.Post;
import kr.co.petmates.api.bussiness.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCareTypeAndArea1AndArea2(String careType, String area1, String area2);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.isBringUp = true WHERE p.id = :id")
    void bringUp(Long id);

    List<Review> findByPetsitterId(Long petsitterId);
}
