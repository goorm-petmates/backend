package kr.co.petmates.api.bussiness.petsitter.service;

import kr.co.petmates.api.bussiness.pet.entity.BookedPet;
import kr.co.petmates.api.bussiness.pet.entity.Pet;
import kr.co.petmates.api.bussiness.petsitter.repository.PetsitterBookingRepository;
import kr.co.petmates.api.bussiness.reserve.dto.BookingDto;
import kr.co.petmates.api.bussiness.reserve.entity.Booking;
import kr.co.petmates.api.enums.BookingStatus;
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
}
