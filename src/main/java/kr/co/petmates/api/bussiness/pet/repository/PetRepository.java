package kr.co.petmates.api.bussiness.pet.repository;

import kr.co.petmates.api.bussiness.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
