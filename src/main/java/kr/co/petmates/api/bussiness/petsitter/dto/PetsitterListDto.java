package kr.co.petmates.api.bussiness.petsitter.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetsitterListDto {

    private Long id;            // 펫시터 게시판 인덱스
    private String nickname;    // 펫시터 닉네임
    private String title;       // 펫시터 게시판 제목
    private String content;     // 펫시터 게시판 내용
    private Integer rating;     // 펫시터 별점
    private Integer count;      // 리뷰 수
    private String roadAddr;    // 펫시터 도로명 주소
    private BigDecimal standardPrice;   // 기준 가격
    private BigDecimal nightPrice;      // 1박 가격
    private Boolean isKakaoProfile;     // 카카오 프로필 이미지 사용 여부
    private String profilePath;         // 카카오 프로필 이미지 URL
    private LocalDateTime orderByDate;  // 끌어올리기 정렬 설정 일자
    private LocalDateTime createDate;   // 작성 일자
    private LocalDateTime modDate;      // 수정 일자

    public static PetsitterListDto toPetsitterListDto(Petsitter petsitter) { //Entity -> DTO
        PetsitterListDto petsitterListDto = new PetsitterListDto();

        petsitterListDto.setId(petsitter.getId());
        petsitterListDto.setNickname(petsitter.getMembers().getNickname());
        petsitterListDto.setTitle(petsitter.getTitle());
        petsitterListDto.setContent(petsitter.getContent());
        petsitterListDto.setRating(petsitter.getAverageRating());
        petsitterListDto.setCount(petsitter.getReviewCnt());
        petsitterListDto.setRoadAddr(petsitter.getMembers().getRoadAddr());
        petsitterListDto.setStandardPrice(petsitter.getStandardPrice());
        petsitterListDto.setNightPrice(petsitter.getNightPrice());
        petsitterListDto.setIsKakaoProfile(petsitter.getIsKakaoProfile());
        petsitterListDto.setProfilePath(petsitter.getProfilePath());
        petsitterListDto.setOrderByDate(petsitter.getOrderByDate());


        return petsitterListDto;
    }
}
