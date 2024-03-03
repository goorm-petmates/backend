package kr.co.petmates.api.bussiness.pet.repository;

import java.util.List;
import kr.co.petmates.api.bussiness.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwnerId(Long id);
}
