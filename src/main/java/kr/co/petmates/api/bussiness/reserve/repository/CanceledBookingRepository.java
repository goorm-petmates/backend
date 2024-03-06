package kr.co.petmates.api.bussiness.reserve.repository;

import kr.co.petmates.api.bussiness.reserve.entity.CanceledBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanceledBookingRepository extends JpaRepository<CanceledBooking, Long> {
}
