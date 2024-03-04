package kr.co.petmates.api.bussiness.reserve.repository;

import kr.co.petmates.api.bussiness.reserve.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByMembersId(Long membersId, Pageable pageable);
}
