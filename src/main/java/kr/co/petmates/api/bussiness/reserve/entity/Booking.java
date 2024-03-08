package kr.co.petmates.api.bussiness.reserve.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.pet.entity.BookedPet;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.common.entity.BaseDateTimeEntity;
import kr.co.petmates.api.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@DynamicUpdate
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOOKING")
@Entity
public class Booking extends BaseDateTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // 시작 날짜

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate; // 종료 날짜

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime; // 시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime; // 종료 시간

    @Column(precision = 15, scale = 5, nullable = false)
    private BigDecimal fee; // 수수료

    @Column(name = "total_price", precision = 15, scale = 5, nullable = false)
    private BigDecimal totalPrice; // 수수료 포함 총 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.BOOK_PREPARED; // 기본값 = BOOK_PREPARED

    // 멤버 테이블과 연결
    @ManyToOne
    @JoinColumn(name = "members_id", nullable = false)
    private Members members;

    // 펫시터와 연결
    @ManyToOne
    @JoinColumn(name = "petsitter_id", nullable = false)
    private Petsitter petsitter;

    // 예약된 펫과 연결
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookedPet> bookedPet = new HashSet<>();

    // 예약 취소 테이블과 연결
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private CanceledBooking canceledBooking;
}


