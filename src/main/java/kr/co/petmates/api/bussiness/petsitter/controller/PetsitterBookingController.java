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
            @RequestParam(value = "page", defaultValue = "0") int page, // 기본 페이지는 첫 번째 페이지
            @RequestParam(value = "size", defaultValue = "3") int size) { // 각 페이지 당 3개의 항목

        List<BookingStatus> requestStatuses = List.of(
                BookingStatus.BOOK_REQUEST,
                BookingStatus.BOOK_APPROVED,
                BookingStatus.BOOK_SUCCESS,
                BookingStatus.BOOK_COMPLETE
        );

        Page<BookingDto> bookingRequests = petsitterBookingService.findBookingRequestsByPetsitterId(
                petsitterId,
                requestStatuses,
                PageRequest.of(page, size, Sort.by("regDate").descending())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        response.put("totalContents", bookingRequests.getTotalElements()); // 전체 데이터의 수
        response.put("pageTotalCnt", bookingRequests.getTotalPages()); // 총 페이지 수
        response.put("pageNum", bookingRequests.getNumber()); // 현재 페이지 번호
        response.put("data", bookingRequests.getContent()); // 현재 페이지 데이터 리스트
        response.put("offset", PageRequest.of(page, size, Sort.by("regAt").descending()).getOffset());

        return ResponseEntity.ok(response);
    }

    // 예약 승인 (BOOK_REQUEST -> BOOK_APPROVED)
    @PostMapping("/approve/{bookingId}")
    public ResponseEntity<?> approveBooking(@PathVariable("bookingId") Long bookingId) {
        try {
            Map<String, String> response = petsitterBookingService.approveBooking(bookingId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("result", "failed");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 예약 거절 (BOOK_REQUEST -> BOOK_REFUSED)
    @PostMapping("/refuse/{bookingId}")
    public ResponseEntity<?> refuseBooking(@PathVariable("bookingId") Long bookingId) {
        try {
            Map<String, String> response = petsitterBookingService.refuseBooking(bookingId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("result", "failed");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
