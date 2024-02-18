package kr.co.petmates.api.bussiness.petsitter.repository;

import java.util.List;
import java.util.Optional;
import kr.co.petmates.api.bussiness.petsitter.dto.PetsitterListDto;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.enums.CareType;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsitterPostRepository extends JpaRepository<Petsitter, Long> {

    boolean existsByMembersId(Long membersId);
    Optional<Petsitter> findByMembersId(Long membersId);

    List<Petsitter> findAll();

    List<Petsitter> findByCareTypeAndArea1AndArea2(CareType careType, String area1, String area2);

}
