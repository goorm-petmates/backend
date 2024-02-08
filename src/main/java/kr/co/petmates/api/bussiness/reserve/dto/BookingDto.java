package kr.co.petmates.api.bussiness.reserve.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import kr.co.petmates.api.bussiness.reserve.entity.Booking;
import kr.co.petmates.api.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BookingDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal fee;
    private BigDecimal totalPrice;
    private BookingStatus status;


    private Long membersId;
    private Long petsitterId;
    private Long canceledBookingId;

    public void setMembersId(Long membersId) {
        this.membersId = membersId;
    }
    public void setPetsitterId(Long petsitterId) {
        this.petsitterId = petsitterId;
    }

    public static BookingDto toBookingDto(Booking booking) { // entity -> dto
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStartDate(booking.getStartDate());
        bookingDto.setEndDate(booking.getEndDate());
        bookingDto.setStartTime(booking.getStartTime());
        bookingDto.setEndTime(booking.getEndTime());
        bookingDto.setFee(booking.getFee());
        bookingDto.setTotalPrice(booking.getTotalPrice());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setPetsitterId(booking.getPetsitter().getId());
        bookingDto.setMembersId(booking.getMembers().getId());

        return bookingDto;
    }
}
