package kr.co.petmates.api.bussiness.pet.repository;

import kr.co.petmates.api.bussiness.pet.entity.PetPhoto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PetPhotoRepository extends JpaRepository<PetPhoto, Long> {
}

