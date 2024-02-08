package kr.co.petmates.api.bussiness.reserve.controller;

import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.reserve.dto.BookingDto;
import kr.co.petmates.api.bussiness.reserve.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reserve")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    // 예약 내역 리스트 조회
    @GetMapping("/{membersId}")
    public ResponseEntity<?> findAllBookings(
            @PathVariable("membersId") Long membersId,
            @RequestParam(value = "page", defaultValue = "0") int page, // 기본 페이지는 첫 번째 페이지
            @RequestParam(value = "size", defaultValue = "3") int size) { // 각 페이지 당 3개의 항목

        Page<BookingDto> bookingPage = bookingService.findBookingsByMemberId(membersId, PageRequest.of(page, size, Sort.by("regDate").descending())); // 최신순 정렬
        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        response.put("totalContents", bookingPage.getTotalElements()); // 전체 데이터의 수
        response.put("pageTotalCnt", bookingPage.getTotalPages()); // 총 페이지 수
        response.put("pageNum", bookingPage.getNumber()); // 현재 페이지의 번호 (페이지 번호 0부터 시작)
        response.put("data", bookingPage.getContent()); // 현재 페이지에 해당하는 데이터 리스트
        response.put("offset", PageRequest.of(page, size, Sort.by("regDate").descending()).getOffset()); // 현재 페이지의 시작 위치 (Offset)

        return ResponseEntity.ok(response);
    }
}
