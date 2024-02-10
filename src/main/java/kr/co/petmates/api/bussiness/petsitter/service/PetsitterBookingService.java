package kr.co.petmates.api.bussiness.petsitter.service;

import java.util.stream.Collectors;
import kr.co.petmates.api.bussiness.pet.entity.BookedPet;
import kr.co.petmates.api.bussiness.pet.repository.BookedPetRepository;
import kr.co.petmates.api.bussiness.petsitter.repository.PetsitterBookingRepository;
import kr.co.petmates.api.bussiness.reserve.dto.BookingDto;
import kr.co.petmates.api.bussiness.reserve.entity.Booking;
import kr.co.petmates.api.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class PetsitterBookingService {
    private final PetsitterBookingRepository petsitterBookingRepository;
    private final BookedPetRepository bookedPetRepository;

    // 펫시터의 예약 요청 리스트 조회 -> BOOK_REQUEST, BOOK_APPROVED, BOOK_SUCCESS, BOOK_COMPLETE 에 해당하는 예약들을 조회
    @Transactional
    public Page<BookingDto> findBookingRequestsByPetsitterId(Long petsitterId, List<BookingStatus> requestStatuses,
                                                             Pageable pageable) {
        Page<Booking> bookingRequestPage = petsitterBookingRepository.findByPetsitterIdAndStatusIn(petsitterId,
                requestStatuses, pageable);

        return bookingRequestPage.map(booking -> {
            BookingDto bookingDto = BookingDto.toBookingDto(booking);

            // 예약에 연결된 펫의 ID를 가져옵니다.
            Long bookedPetId = booking.getBookedPet().iterator().next().getPet().getId();
            bookingDto.setBookedPetId(bookedPetId); // 해당 펫의 ID를 설정합니다.

            return bookingDto;
        });
    }
}
