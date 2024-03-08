package kr.co.petmates.api.bussiness.reserve.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseCancelBookingDto {
    private final String result;

    private final Object data;

    // 성공 데이터 객체
    @Builder
    @Getter
    public static class SuccessData {
        private final Long id;
        private final Long bookId;
        private final String code;
        private final LocalDateTime canceledTime;
        private final String status;
    }

    // 실패 데이터 객체
    @Builder
    @Getter
    public static class FailedData {
        private final String reason;
    }
}

