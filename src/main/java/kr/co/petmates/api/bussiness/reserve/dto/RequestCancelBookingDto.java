package kr.co.petmates.api.bussiness.reserve.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCancelBookingDto {
    private Long id; // 예약 ID

    private String code; // 취소 사유 코드

    // "canceledTime": "2024-03-06T06:54:25" 대신 "canceledTime": "2024-03-06 06:54:25"처럼 처리
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime canceledTime; // 취소 시간
}

