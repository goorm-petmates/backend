package kr.co.petmates.api.bussiness.petsitter.repository;

import java.util.List;
import java.util.Optional;
import kr.co.petmates.api.bussiness.petsitter.dto.PetsitterDto.PetsitterProjection;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.bussiness.review.entity.Review;
import kr.co.petmates.api.enums.CareType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PetsitterPostRepository extends JpaRepository<Petsitter, Long> {

    boolean existsByMembersId(Long membersId);
    Optional<Petsitter> findByMembersId(Long membersId);

    List<Petsitter> findAll();

    List<Petsitter> findByCareTypeAndArea1AndArea2(CareType careType, String area1, String area2);

    List<PetsitterProjection> findAllProjectedBy(); // 프로젝션을 사용한 메소드

}
