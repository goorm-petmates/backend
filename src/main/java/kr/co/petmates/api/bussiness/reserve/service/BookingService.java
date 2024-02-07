package kr.co.petmates.api.bussiness.reserve.service;

import kr.co.petmates.api.bussiness.reserve.dto.BookingDto;
import kr.co.petmates.api.bussiness.reserve.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    // 예약 내역 리스트 조회
    public Page<BookingDto> findBookingsByMemberId(Long memberId, Pageable pageable) {
        return bookingRepository.findByMembersId(memberId, pageable).map(BookingDto::toBookingDto);
    }
}
