package kr.co.petmates.api.enums;

import lombok.Getter;

@Getter
public enum BookingStatus {
    BOOK_PREPARED("예약 대기"),
    BOOK_REQUEST("예약 요청"),
    BOOK_APPROVED("예약 승인"),
    BOOK_REFUSED("예약 거절"),
    BOOK_SUCCESS("예약 성공"),
    BOOK_CANCELED("예약 취소"),
    BOOK_REFUND("환불 완료"),
    BOOK_COMPLETE("이용 완료");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }
}
