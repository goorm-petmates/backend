package kr.co.petmates.api.bussiness.petsitter.repository;

import java.util.List;
import kr.co.petmates.api.bussiness.reserve.entity.Booking;
import kr.co.petmates.api.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetsitterBookingRepository extends JpaRepository<Booking, Long> {

    // 펫시터의 예약 요청 리스트 조회-> BOOK_REQUEST, BOOK_APPROVED, BOOK_SUCCESS, BOOK_COMPLETE 에 해당하는 예약들을 조회
    Page<Booking> findByPetsitterIdAndStatusIn(Long petsitterId, List<BookingStatus> requestStatuses, Pageable pageable);
}
