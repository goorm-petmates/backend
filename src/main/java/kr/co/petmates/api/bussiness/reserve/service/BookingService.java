package kr.co.petmates.api.bussiness.reserve.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.bussiness.petsitter.repository.PetsitterPostRepository;
import kr.co.petmates.api.bussiness.reserve.dto.BookingDto;
import kr.co.petmates.api.bussiness.reserve.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final MembersRepository membersRepository;
    private final PetsitterPostRepository petsitterPostRepository;

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
    public Map<String, Object> save(BookingDto bookingDto) {
        bookingDto.setMembersId(2L); // @Todo: 회원 인덱스 대신 JWT 토큰 사용하도록 수정하기
        Members members = membersRepository.findById(bookingDto.getMembersId())
                        .orElseThrow(()
                -> new EntityNotFoundException("Members not found with id: " + bookingDto.getMembersId())
        );
        Petsitter petsitter = petsitterPostRepository.findById(bookingDto.getPetsitterId())
                        .orElseThrow(()
                                -> new EntityNotFoundException("Petsitter not found with id: "
                                + bookingDto.getPetsitterId())
        );

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id",bookingDto.getId());
        Long bookId = bookingRepository.save(bookingDto.toEntity(members, petsitter)).getId();
        resultMap.put("bookId", bookId);
        resultMap.put("status", bookingDto.getStatus());

        return resultMap;
    }
}
