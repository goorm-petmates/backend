package kr.co.petmates.api.bussiness.pet.dto;

import kr.co.petmates.api.bussiness.pet.entity.PetPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetPhotoDto {

    private Long id; // 시퀀스
    private String storedFileName; // 파일의 URL을 저장할 필드
    private Long petId; // 펫 정보를 참조하기 위한 ID

    public static PetPhotoDto toPetPhotoDto(PetPhoto petPhoto) { // entity -> dto
        PetPhotoDto petPhotoDto = new PetPhotoDto();
        petPhotoDto.setStoredFileName(petPhoto.getStoredFileName());

        return petPhotoDto;
    }
}
