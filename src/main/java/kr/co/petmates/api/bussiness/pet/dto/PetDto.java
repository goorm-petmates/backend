package kr.co.petmates.api.bussiness.pet.dto;

import com.nimbusds.openid.connect.sdk.claims.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetDto {

    private Long id; // 시퀀스

    @NotNull
    @Pattern(regexp = "^[가-힣_]{1,10}$")
    private String name; // 이름

    @NotNull
    @Pattern(regexp = "^[가-힣_]{1,10}$")
    private String breed; // 견종

    @NotNull
    @Pattern(regexp = "^(19[0-9]{2}|20[0-2][0-9]|202[0-4])$", message = "출생년도는 1900년부터 2024년 사이여야 합니다.")
    private String birthYear; // 출생년도

    @NotNull
    @Pattern(regexp = "^[0-9]{1,2}$|^(100)$\n")
    private String weight; // 몸무게

    private Gender sex; // 성별

    private boolean isNeutering; // 중성화

    private boolean isAllergy; // 알러지

    private boolean isDisease; // 질병

    private String ect; // 참고사항
}

