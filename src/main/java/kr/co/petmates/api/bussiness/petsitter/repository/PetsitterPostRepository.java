package kr.co.petmates.api.bussiness.petsitter.repository;

import java.util.Optional;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsitterPostRepository extends JpaRepository<Petsitter, Long> {

    boolean existsByMembersId(Long membersId);
    Optional<Petsitter> findByMembersId(Long membersId);
}
