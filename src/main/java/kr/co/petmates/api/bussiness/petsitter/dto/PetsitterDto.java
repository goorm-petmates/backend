package kr.co.petmates.api.bussiness.petsitter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.enums.CareType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetsitterDto {
    private Long id;                // 시퀀스
    private String title;           // 제목
    private String content;         // 내용
    private BigDecimal standardPrice;  // 기준 가격
    private BigDecimal addPrice;       // 추가 가격
    private BigDecimal nightPrice;     // 1박 가격
    private Integer viewCnt;        // 조회수
    private Integer reviewCnt;      // 리뷰수
    private Integer averageRating;  // 평균 리뷰 평점
    private LocalDate orderByDate;  // 정렬 기준 날짜
    private Boolean isKakaoProfile; // 카카오 소셜 프로필 사용 여부
    private String profilePath;     // 소셜 프로필 이미지 경로
    private CareType careType;      // 데이케어, 1박케어
    private String area1;           // 시 단위 지역
    private String area2;           // 구 단위 지역
    private Boolean isBringUp;      // 끌어올림 여부
    
    public static PetsitterDto toPetsitterDto(Petsitter petsitter) { //Entity -> DTO
        PetsitterDto petsitterDto = new PetsitterDto();
        petsitterDto.setId(petsitter.getId());
        petsitterDto.setTitle(petsitter.getTitle());
        petsitterDto.setContent(petsitter.getContent());
        petsitterDto.setStandardPrice(petsitter.getStandardPrice());
        petsitterDto.setAddPrice(petsitter.getAddPrice());
        petsitterDto.setNightPrice(petsitter.getNightPrice());
        petsitterDto.setViewCnt(petsitter.getViewCnt());
        petsitterDto.setReviewCnt(petsitter.getReviewCnt());
        petsitterDto.setAverageRating(petsitter.getAverageRating());
        petsitterDto.setOrderByDate(petsitter.getOrderByDate());
        petsitterDto.setIsKakaoProfile(petsitter.getIsKakaoProfile());
        petsitterDto.setProfilePath(petsitter.getProfilePath());
        petsitterDto.setCareType(petsitter.getCareType());
        petsitterDto.setArea1(petsitter.getArea1());
        petsitterDto.setArea2(petsitter.getArea2());
        petsitterDto.setIsBringUp(petsitter.getIsBringUp());

        return petsitterDto;
    }
}

