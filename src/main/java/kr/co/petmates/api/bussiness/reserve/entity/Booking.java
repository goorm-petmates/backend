package kr.co.petmates.api.bussiness.reserve.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.common.entity.BaseDateTimeEntity;
import kr.co.petmates.api.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "booking")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Getter
@Setter
@ToString
public class Booking extends BaseDateTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt; // 등록일시

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // 시작 날짜

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate; // 종료 날짜

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime; // 시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime; // 종료 시간

    @Column(nullable = false)
    private BigDecimal fee; // 수수료

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice; // 수수료 포함 총 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.BOOK_PREPARED; // 기본값 = BOOK_PREPARED


    @ManyToOne
    @JoinColumn(name = "members_id", nullable = false)
    private Members members;

    // 예약 취소 테이블과 연결
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private CanceledBooking canceledBooking;

    @ManyToOne
    @JoinColumn(name = "petsitter_id", nullable = false)
    private Petsitter petsitter;
}
