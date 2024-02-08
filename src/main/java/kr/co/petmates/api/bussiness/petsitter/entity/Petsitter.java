package kr.co.petmates.api.bussiness.petsitter.entity;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.common.entity.BaseDateTimeEntity;
import kr.co.petmates.api.enums.CareType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Builder
@Table(name = "PETSITTER")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
//@JsonIgnoreProperties({ "pwd" })
@Getter
@Setter
@ToString
public class Petsitter extends BaseDateTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;                //  시퀀스

    @Column(nullable = false)
    private String title;           //  제목

    @Column(nullable = false)
    private String content;         //  내용

    @Column(nullable = false)
    private BigDecimal standardPrice;  //  기준 가격

    @Column(nullable = false)
    private BigDecimal addPrice;       //  추가 가격

    @Column(nullable = false)
    private BigDecimal nightPrice;     //  1박 가격

    @Column(nullable = false)
    private Integer viewCnt;        //  조회수

    @Column(nullable = false)
    private Integer reviewCnt;      //  리뷰수

    @Column(nullable = false)
    private Integer averageRating;  //  평균 리뷰 평점

    @Column(name ="order_by_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime orderByDate;  //  정렬 기준 날짜

    @Column(nullable = false)
    private Boolean isKakaoProfile; //  카카오 소셜 프로필 사용 여부

    @Column
    private String profilePath;     //  소셜 프로필 이미지 경로

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CareType careType;      //  DayCare, NightCare

    @Column(nullable = false)
    private String area1;           //  시 단위 지역

    @Column(nullable = false)
    private String area2;           //  구 단위 지역

    @Column(nullable = false)
    private Boolean isBringUp;      // 끌어올림 여부

    // 멤버와 연결
    @OneToOne
    @JoinColumn(name = "members_id")
    private Members members;
}
