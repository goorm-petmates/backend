package kr.co.petmates.api.bussiness.reserve.controller;

import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.reserve.dto.BookingRequestDto;
import kr.co.petmates.api.bussiness.reserve.dto.BookingResponseDto;
import kr.co.petmates.api.bussiness.reserve.service.BookingRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/petsitter")
@RequiredArgsConstructor
public class BookingRequestController {
    private final BookingRequestService bookingRequestService;
    @PostMapping("/reserve")
    public ResponseEntity<Map<String, Object>> createBooking(@RequestBody BookingRequestDto bookingRequestDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            BookingResponseDto bookingResponseDto = bookingRequestService.createBooking(bookingRequestDto);
            response.put("result", "success");
            response.put("data", bookingResponseDto); // 예약 성공 데이터 추가
            // 예시에서 bookingPage.getTotalElements()는 사용하지 않습니다.
            // 필요에 따라 다른 정보 추가 가능
        } catch (Exception e) {
            response.put("result", "failed");
            response.put("reason", e.getMessage()); // 실패 이유 추가
        }
        return ResponseEntity.ok(response);
    }
}
