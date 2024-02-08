package kr.co.petmates.api.bussiness.petsitter.post.repository;

import kr.co.petmates.api.bussiness.petsitter.post.entity.Petsitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsitterRepository extends JpaRepository<Petsitter, Long>  {
}
