package kr.co.petmates.api.bussiness.reserve.service;

import java.time.LocalDateTime;
import java.util.Optional;
import kr.co.petmates.api.bussiness.reserve.dto.BookingDto;
import kr.co.petmates.api.bussiness.reserve.entity.Booking;
import kr.co.petmates.api.bussiness.reserve.entity.CanceledBooking;
import kr.co.petmates.api.bussiness.reserve.repository.BookingRepository;
import kr.co.petmates.api.bussiness.reserve.repository.CanceledBookingRepository;
import kr.co.petmates.api.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CanceledBookingRepository canceledBookingRepository;

    // 예약 내역 리스트 조회
    public Page<BookingDto> findBookingsByMemberId(Long memberId, Pageable pageable) {
        return bookingRepository.findByMembersId(memberId, pageable).map(BookingDto::toBookingDto);
    }

    /**
     * 예약 요청 확인
     * @param bookingId Long -
     *                  the primary key of table 'booking'
     * @return Optional&lt;BookingDto&gt; -
     *                  the bookingDto wrapped in an {@link Optional}
     */
    @Transactional(readOnly = true)
    public Optional<BookingDto> findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).map(BookingDto::toBookingDto);
    }

    @Transactional
    public CanceledBooking cancelBooking(Long bookingId, LocalDateTime canceledTime) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getCanceledBooking() != null) {
            throw new RuntimeException("Booking already canceled");
        }

        booking.setStatus(BookingStatus.BOOK_CANCELED);

        CanceledBooking canceledBooking = new CanceledBooking();
        canceledBooking.setBooking(booking);
        canceledBooking.setFee(booking.getFee());
        canceledBooking.setAmount(booking.getTotalPrice().subtract(booking.getFee()));
        canceledBooking.setCanceledTime(canceledTime);
        booking.setCanceledBooking(canceledBooking);

        bookingRepository.save(booking);

        return canceledBooking;
    }
}
