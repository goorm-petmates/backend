package kr.co.petmates.api.bussiness.pet.repository;

import java.util.List;
import kr.co.petmates.api.bussiness.pet.entity.BookedPet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedPetRepository extends JpaRepository<BookedPet, Long> {
    List<BookedPet> findByBookingId(Long bookingId);
}
