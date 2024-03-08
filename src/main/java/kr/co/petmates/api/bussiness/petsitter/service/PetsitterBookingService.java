package kr.co.petmates.api.bussiness.petsitter.service;

import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.pet.entity.BookedPet;
import kr.co.petmates.api.bussiness.pet.entity.Pet;
import kr.co.petmates.api.bussiness.petsitter.repository.PetsitterBookingRepository;
import kr.co.petmates.api.bussiness.reserve.dto.BookingDto;
import kr.co.petmates.api.bussiness.reserve.entity.Booking;
import kr.co.petmates.api.enums.BookingStatus;
import kr.co.petmates.api.enums.UserInterfaceMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class PetsitterBookingService {
    private final PetsitterBookingRepository petsitterBookingRepository;

    // 예약 요청 및 취소 리스트 조회를 위한 공통 조회 로직 메서드 구현
    // 예약 요청 리스트 조회 -> BOOK_REQUEST, BOOK_APPROVED, BOOK_SUCCESS, BOOK_COMPLETE 에 해당하는 예약들을 조회
    // 예약 취소 리스트 조회 -> BOOK_REFUSED, BOOK_CANCELED, BOOK_REFUND 에 해당하는 예약들을 조회

    @Transactional
    public Page<BookingDto> findBookingRequestsByPetsitterId(Long petsitterId, List<BookingStatus> requestStatuses,
                                                             Pageable pageable) {
        return petsitterBookingRepository.findByPetsitterIdAndStatusIn(petsitterId, requestStatuses, pageable)
                .map(this::convertToBookingDto);
    }

    private BookingDto convertToBookingDto(Booking booking) {
        BookingDto bookingDto = BookingDto.toBookingDto(booking);

        Long bookedPetId = booking.getBookedPet().stream() // BookedPet ID를 찾아서 BookingDto에 설정
                .findFirst() // 첫 번째 BookedPet 객체를 가져옴
                .map(BookedPet::getPet) // BookedPet에서 Pet 객체를 얻음
                .map(Pet::getId) // Pet 객체에서 ID를 얻음
                .orElse(null); // 만약 BookedPet 객체가 없다면 null을 반환

        bookingDto.setBookedPetId(bookedPetId);

        return bookingDto;
    }


    // 예약 승인 (BOOK_REQUEST -> BOOK_APPROVED)
    public Map<String, String> approveBooking(Long bookingId) {
        return changeBookingStatus(bookingId, BookingStatus.BOOK_APPROVED);
    }

    // 예약 거절 (BOOK_REQUEST -> BOOK_REFUSED)
    public Map<String, String> refuseBooking(Long bookingId) {
        return changeBookingStatus(bookingId, BookingStatus.BOOK_REFUSED);
    }

    // 예약 승인 및 거절을 위한 공통 메소드
    @Transactional
    public Map<String, String> changeBookingStatus(Long bookingId, BookingStatus newStatus) {
        Booking booking = petsitterBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalStateException(UserInterfaceMsg.ERR_NOT_EXIST_RESERVED.getValue()));

        Map<String, String> result = new HashMap<>();

        if (booking.getStatus() == BookingStatus.BOOK_REQUEST) {
            booking.setStatus(newStatus);
            petsitterBookingRepository.save(booking);
            result.put("result", "success");
            result.put("status", newStatus.name());
        } else {
            // 예약 상태가 BOOK_REQUEST가 아닐 경우, 상태 변경을 시도하지 않고 결과를 반환
            result.put("result", "failed");
            result.put("status", booking.getStatus().name());
        }

        return result;
    }
}
