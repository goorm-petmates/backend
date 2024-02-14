package kr.co.petmates.api.bussiness.petsitter.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import kr.co.petmates.api.bussiness.petsitter.service.PetsitterBookingService;
import kr.co.petmates.api.bussiness.reserve.dto.BookingDto;
import kr.co.petmates.api.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/my-page/petsitter")
@RequiredArgsConstructor
public class PetsitterBookingController {
    private final PetsitterBookingService petsitterBookingService;

    // 펫시터의 예약 요청 리스트 조회 -> BOOK_REQUEST, BOOK_APPROVED, BOOK_SUCCESS, BOOK_COMPLETE 에 해당하는 예약들을 조회
    @GetMapping("/reserve/{petsitterId}")
    public ResponseEntity<?> getBookingRequests(
            @PathVariable("petsitterId") Long petsitterId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size) {

        List<BookingStatus> requestStatuses = List.of(
                BookingStatus.BOOK_REQUEST,
                BookingStatus.BOOK_APPROVED,
                BookingStatus.BOOK_SUCCESS,
                BookingStatus.BOOK_COMPLETE
        );

        return getBookingRequestsByStatus(petsitterId, requestStatuses, page, size);
    }

    // 펫시터의 예약 취소 리스트 조회 -> BOOK_REFUSED, BOOK_CANCELED, BOOK_REFUND 에 해당하는 예약들을 조회
    @GetMapping("/cancel/{petsitterId}")
    public ResponseEntity<?> getCanceledBooking(
            @PathVariable("petsitterId") Long petsitterId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size) {

        List<BookingStatus> CanceledStatuses = List.of(
                BookingStatus.BOOK_REFUSED,
                BookingStatus.BOOK_CANCELED,
                BookingStatus.BOOK_REFUND
        );

        return getBookingRequestsByStatus(petsitterId, CanceledStatuses, page, size);
    }
    // 예약 요청 및 취소 리스트 조회를 위한 공통 조회 로직 메서드 구현
    private ResponseEntity<?> getBookingRequestsByStatus(
            Long petsitterId,
            List<BookingStatus> statuses,
            int page,
            int size) {

        Page<BookingDto> bookingRequests = petsitterBookingService.findBookingRequestsByPetsitterId(
                petsitterId,
                statuses,
                PageRequest.of(page, size, Sort.by("regDate").descending())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        response.put("totalContents", bookingRequests.getTotalElements());
        response.put("pageTotalCnt", bookingRequests.getTotalPages());
        response.put("pageNum", bookingRequests.getNumber());
        response.put("data", bookingRequests.getContent());
        response.put("offset", PageRequest.of(page, size, Sort.by("regAt").descending()).getOffset());

        return ResponseEntity.ok(response);
    }


    // 예약 승인 (BOOK_REQUEST -> BOOK_APPROVED)
    @PostMapping("/approve/{bookingId}")
    public ResponseEntity<?> approveBooking(@PathVariable("bookingId") Long bookingId) {
        return handleBookingOperation(bookingId, petsitterBookingService::approveBooking);
    }

    // 예약 거절 (BOOK_REQUEST -> BOOK_REFUSED)
    @PostMapping("/refuse/{bookingId}")
    public ResponseEntity<?> refuseBooking(@PathVariable("bookingId") Long bookingId) {
        return handleBookingOperation(bookingId, petsitterBookingService::refuseBooking);
    }

    // 예약 승인 및 거절을 위한 공통 메소드
    private ResponseEntity<?> handleBookingOperation(Long bookingId, Function<Long, Map<String, String>> operation) {
        try {
            Map<String, String> response = operation.apply(bookingId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("result", "failed");
            errorResponse.put("reason", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
