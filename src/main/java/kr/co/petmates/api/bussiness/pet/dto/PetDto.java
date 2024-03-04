package kr.co.petmates.api.bussiness.pet.dto;

import com.nimbusds.openid.connect.sdk.claims.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import kr.co.petmates.api.bussiness.pet.entity.Pet;
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
    private Long membersId; // 멤버를 참조하기 위한 ID
    private Long petPhotoId;
    private Long bookedPetId;

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

    private String etc; // 참고사항

    private String photoUrl; // 사진 접근을 위한 URL


    public static PetDto toPetDto(Pet pet) { // entity -> dto
        PetDto petDto = new PetDto();
        petDto.setName(pet.getName());
        petDto.setBreed(pet.getBreed());
        petDto.setBirthYear(pet.getBirthYear());
        petDto.setWeight(pet.getWeight());
        petDto.setSex(pet.getSex());
        petDto.setEtc(pet.getEtc());

        return petDto;
    }
}

