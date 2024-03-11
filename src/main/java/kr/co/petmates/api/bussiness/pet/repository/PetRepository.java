package kr.co.petmates.api.bussiness.pet.repository;

import java.util.List;
import kr.co.petmates.api.bussiness.pet.dto.PetResponseDto;
import kr.co.petmates.api.bussiness.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner_Id(Long ownerId);

//    @Query("SELECT p FROM Pet p JOIN FETCH p.owner o LEFT JOIN FETCH p.petPhoto pp WHERE o.id = :ownerId")
    @Query("SELECT new kr.co.petmates.api.bussiness.pet.dto.PetResponseDto(p.id, p.owner.id, p.petPhoto.id, "
            + "p.name, p.breed, p.birthYear, p.weight, p.isNeutering, p.isAllergy, p.isDisease, p.etc, "
            + "p.petPhoto.storedFileName) "
            + "FROM Pet p LEFT JOIN PetPhoto pp ON p.id = pp.pet.id "
            + "WHERE p.owner.id = :ownerId and p.isDeleted = false")
    List<PetResponseDto> findByMembersId(@Param("ownerId") Long ownerId);


}
