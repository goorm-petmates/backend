package kr.co.petmates.api.bussiness.reserve.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import kr.co.petmates.api.bussiness.reserve.entity.Booking;
import kr.co.petmates.api.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal fee;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private Long memberId;
    private Long petSitterId;
    private Long canceledBookingId;


    public static BookingDto toBookingDto(Booking booking) { // entity -> dto
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStartDate(booking.getStartDate());
        bookingDto.setEndDate(booking.getEndDate());
        bookingDto.setStartTime(booking.getStartTime());
        bookingDto.setEndTime(booking.getEndTime());
        bookingDto.setFee(booking.getFee());
        bookingDto.setTotalPrice(booking.getTotalPrice());
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }
}
