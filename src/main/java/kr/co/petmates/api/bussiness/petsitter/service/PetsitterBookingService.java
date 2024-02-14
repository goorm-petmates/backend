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

    // 펫시터의 예약 요청 리스트 조회 -> BOOK_REQUEST, BOOK_APPROVED, BOOK_SUCCESS, BOOK_COMPLETE 에 해당하는 예약들을 조회
    @Transactional
    public Page<BookingDto> findBookingRequestsByPetsitterId(Long petsitterId, List<BookingStatus> requestStatuses,
                                                             Pageable pageable) {
        Page<Booking> bookingRequestPage = petsitterBookingRepository.findByPetsitterIdAndStatusIn(petsitterId,
                requestStatuses, pageable);

        return bookingRequestPage.map(booking -> {
            BookingDto bookingDto = BookingDto.toBookingDto(booking);

            Long bookedPetId = booking.getBookedPet().stream()
                    .findFirst() // 첫 번째 BookedPet 객체를 가져옵니다.
                    .map(BookedPet::getPet) // BookedPet에서 Pet 객체를 얻습니다.
                    .map(Pet::getId) // Pet 객체에서 ID를 얻습니다.
                    .orElse(null); // 만약 BookedPet 객체가 없다면 null을 반환합니다.
            bookingDto.setBookedPetId(bookedPetId);

            return bookingDto;
        });
    }
    // 예약 승인 (BOOK_REQUEST -> BOOK_APPROVED)
    @Transactional
    public Map<String, String> approveBooking(Long bookingId) throws Exception {
        Booking booking = petsitterBookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception(UserInterfaceMsg.ERR_NOT_EXIST_RESERVED.getValue()));

        Map<String, String> result = new HashMap<>();

        if (booking.getStatus() == BookingStatus.BOOK_REQUEST) {
            booking.setStatus(BookingStatus.BOOK_APPROVED);
            petsitterBookingRepository.save(booking);
            result.put("result", "success");
            result.put("status", BookingStatus.BOOK_APPROVED.name());
        } else {
            // 예약 상태가 BOOK_REQUEST가 아닐 경우, 상태 변경을 시도하지 않고 결과를 반환
            result.put("result", "failed");
            result.put("status", booking.getStatus().name());
        }

        return result;
    }

    // 예약 거절 (BOOK_REQUEST -> BOOK_REFUSED)
    @Transactional
    public Map<String, String> refuseBooking(Long bookingId) throws Exception {
        Booking booking = petsitterBookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception(UserInterfaceMsg.ERR_NOT_EXIST_RESERVED.getValue()));

        Map<String, String> result = new HashMap<>();

        if (booking.getStatus() == BookingStatus.BOOK_REQUEST) {
            booking.setStatus(BookingStatus.BOOK_REFUSED);
            petsitterBookingRepository.save(booking);
            result.put("result", "success");
            result.put("status", BookingStatus.BOOK_REFUSED.name());
        } else {
            result.put("result", "failed");
            result.put("status", booking.getStatus().name());
        }

        return result;
    }
}
