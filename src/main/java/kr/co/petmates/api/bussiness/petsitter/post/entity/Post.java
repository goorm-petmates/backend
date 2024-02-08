package kr.co.petmates.api.bussiness.petsitter.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.petsitter.post.dto.PostSearchRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@JsonIgnoreProperties({ "pwd" })
@Getter
@Setter
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;                //  시퀀스

    // 펫시터와 Members -> MembersId 연결
    @Getter
    @ManyToOne
    @JoinColumn(name = "membersId")
    private Members petsitter;

    public void setOwner(Members petsitter) {
        this.petsitter = petsitter;
    }

    @Column(nullable = false)
    private String title;           //  제목

    @Column(nullable = false)
    private String content;         //  내용

    @Column(nullable = false)
    private Integer standardPrice;  //  기준 가격

    @Column(nullable = false)
    private Integer addPrice;       //  추가 가격

    @Column(nullable = false)
    private Integer nightPrice;     //  1박 가격

    @Column(nullable = false)
    private Integer viewCnt;        //  조회수

    @Column(nullable = false)
    private Integer reviewCnt;      //  리뷰수

    @Column(nullable = false)
    private Integer averageRating;  //  평균 리뷰 평점

    @Column(nullable = false)
    private String createAt;        //  등록 일시

    @Column(nullable = false)
    private String modAt;           //  수정 일시

    @Column(nullable = false)
    private String orderByAt;       //  정렬

    @Column(nullable = false)
    private boolean isKakaoProfile; //  카카오 소셜 프로필 사용 여부

    @Column
    private String profilePath;     //  소셜 프로필 이미지 경로

    @Column(nullable = false)
    private String careType;        //  데이케어, 1박케어

    @Column(nullable = false)
    private String area1;           //  시 단위 지역

    @Column(nullable = false)
    private String area2;           //  구 단위 지역

    @Column(nullable = false)
    private boolean isBringUp;      // 끌어올림 여부


}
