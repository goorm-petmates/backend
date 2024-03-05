package kr.co.petmates.api.bussiness.reserve.service;

import java.util.HashSet;
import java.util.Set;
import kr.co.petmates.api.bussiness.pet.entity.BookedPet;
import kr.co.petmates.api.bussiness.pet.entity.Pet;
import kr.co.petmates.api.bussiness.pet.repository.PetRepository;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.bussiness.petsitter.repository.PetsitterPostRepository;
import kr.co.petmates.api.bussiness.reserve.dto.BookingRequestDto;
import kr.co.petmates.api.bussiness.reserve.dto.BookingResponseDto;
import kr.co.petmates.api.bussiness.reserve.entity.Booking;
import kr.co.petmates.api.bussiness.reserve.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingRequestService {
    private final BookingRepository bookingRepository;
    private final PetsitterPostRepository petsitterPostRepository;
    private final PetRepository petRepository;

    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) throws Exception {
        // Petsitter 객체를 검증합니다. 해당 ID를 가진 Petsitter가 없으면 예외를 발생시킵니다.
        Petsitter petsitter = petsitterPostRepository.findById(bookingRequestDto.getPetsitterId())
                .orElseThrow(() -> new Exception("Petsitter not found"));

        // Booking 객체를 생성합니다. DTO로부터 받은 정보를 사용하여 Booking 객체를 빌드합니다.
        Booking booking = Booking.builder()
                .startDate(bookingRequestDto.getStartDate()) // 시작 날짜를 할당합니다.
                .endDate(bookingRequestDto.getEndDate()) // 종료 날짜를 할당합니다.
                .startTime(bookingRequestDto.getStartTime()) // 시작 시간을 할당합니다.
                .endTime(bookingRequestDto.getEndTime()) // 종료 시간을 할당합니다.
                .fee(bookingRequestDto.getFee()) // 수수료를 할당합니다.
                .totalPrice(bookingRequestDto.getTotalPrice()) // 총 가격을 할당합니다.
                .petsitter(petsitter) // Petsitter 객체를 연결합니다.
                .build(); // Booking 객체를 생성합니다.

        // 예약된 반려동물들을 저장할 Set을 초기화합니다.
        Set<BookedPet> bookedPets = new HashSet<>();
        for (Long petId : bookingRequestDto.getReservedPets()) {
            // 각 반려동물 ID에 해당하는 Pet 객체를 찾습니다. 찾을 수 없으면 예외를 발생시킵니다.
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new Exception("Pet not found"));
            // BookedPet 객체를 생성하고, Pet 객체와 Booking 객체를 연결합니다.
            BookedPet bookedPet = new BookedPet();
            bookedPet.setPet(pet);
            bookedPet.setBooking(booking);
            bookedPets.add(bookedPet); // 생성한 BookedPet 객체를 Set에 추가합니다.
        }
        // Booking 객체에 BookedPet들을 연결합니다.
        booking.setBookedPet(bookedPets);

        // 생성된 Booking 객체를 데이터베이스에 저장합니다.
        booking = bookingRepository.save(booking);

        // 응답 DTO를 준비합니다. 생성된 예약의 ID, 상태, 메시지를 포함합니다.
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setReservationId(booking.getId().toString()); // 예약 ID
        bookingResponseDto.setStatus(booking.getStatus().name()); // 예약 상태
        bookingResponseDto.setMessage("예약 신청이 접수되었습니다."); // 응답 메시지
        return bookingResponseDto; // 준비된 응답 DTO를 반환합니다.
    }
}
