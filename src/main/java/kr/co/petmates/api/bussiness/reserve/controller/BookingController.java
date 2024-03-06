package kr.co.petmates.api.bussiness.reserve.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import kr.co.petmates.api.bussiness.reserve.dto.BookingDto;
import kr.co.petmates.api.bussiness.reserve.dto.ResponseFailedDto;
import kr.co.petmates.api.bussiness.reserve.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

        Page<BookingDto> bookingPage = bookingService.findBookingsByMemberId(membersId, PageRequest.of(page, size, Sort.by("createDate").descending())); // 최신순 정렬
        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        response.put("totalContents", bookingPage.getTotalElements()); // 전체 데이터의 수
        response.put("pageTotalCnt", bookingPage.getTotalPages()); // 총 페이지 수
        response.put("pageNum", bookingPage.getNumber()); // 현재 페이지의 번호 (페이지 번호 0부터 시작)
        response.put("data", bookingPage.getContent()); // 현재 페이지에 해당하는 데이터 리스트
        response.put("offset", PageRequest.of(page, size, Sort.by("createDate").descending()).getOffset()); // 현재 페이지의 시작 위치 (Offset)

        return ResponseEntity.ok(response);
    }

    /**
     * 예약 요청 확인
     * @param bookingId Long - 예약건 목록 화면에서 선택한 특정 예약 건의 id, booking테이블의 id
     * @return ResponseEntity<?>
     */
    @GetMapping("/check/{bookingId}")
    public ResponseEntity<?> findBookingById(@PathVariable("bookingId") Long bookingId) {
        Optional<BookingDto> bookingDto = bookingService.findBookingById(bookingId);
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Object> response = bookingDto
                .map(dto -> {
                    responseMap.put("result", "success");
                    responseMap.put("data", dto);
                    return responseMap;
                })
                .orElseGet(() -> {
                    ResponseFailedDto responseFailedDto = ResponseFailedDto.builder("ERR_NOT_EXIST_POST").build();
                    responseMap.put("result", responseFailedDto.getResult());
                    responseMap.put("data", responseFailedDto.getData());

                    return responseMap;
                });
        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply")
    public ResponseEntity<?> bookingApply(@RequestBody BookingDto bookingDto) {
        Map<String, Object> responseMap = bookingService.save(bookingDto);
        Map<String, Object> response = new HashMap<>();
        if ((Long) responseMap.get("bookId") > 1L) {
            response.put("result", "success");
            response.put("data", responseMap);
        } else {
            response.put("result", "failed");
            response.put("data", Map.of("reason", "BOOK_")); // @Todo: 처리에 따른 상태코드 업데이트
        }


        return ResponseEntity.ok(response);
    }
}
