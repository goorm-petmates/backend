package kr.co.petmates.api.bussiness.pet.dto;

import kr.co.petmates.api.bussiness.pet.entity.Pet;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetResponseDto {

    private Long id; // 시퀀스

    private Long membersId; // 멤버를 참조하기 위한 ID

    private Long petPhotoId;

    private String name; // 이름

    private String breed; // 견종

    private String birthYear; // 출생년도

    private String weight; // 몸무게

//    private Gender sex; // 성별

    private boolean isNeutering; // 중성화

    private boolean isAllergy; // 알러지

    private boolean isDisease; // 질병

    private String etc; // 참고사항

    private String photoUrl; // 사진 접근을 위한 URL

    @Builder
    public PetResponseDto(Long id, Long membersId, Long petPhotoId, String name, String breed, String birthYear,
                          String weight, /* Gender sex, */ boolean isNeutering, boolean isAllergy, boolean isDisease,
                          String etc, String photoUrl) {
        this.id = id;
        this.membersId = membersId;
        this.petPhotoId = petPhotoId;
        this.name = name;
        this.breed = breed;
        this.birthYear = birthYear;
        this.weight = weight;
//        this.sex = sex;
        this.isNeutering = isNeutering;
        this.isAllergy = isAllergy;
        this.isDisease = isDisease;
        this.etc = etc;
        this.photoUrl = photoUrl;
    }


    public static PetResponseDto toPetDto(Pet pet) { // entity -> dto
        PetResponseDto petDto = new PetResponseDto();
        petDto.setName(pet.getName());
        petDto.setBreed(pet.getBreed());
        petDto.setBirthYear(pet.getBirthYear());
        petDto.setWeight(pet.getWeight());
//        petDto.setSex(pet.getSex());
        petDto.setEtc(pet.getEtc());

        return petDto;
    }
}

