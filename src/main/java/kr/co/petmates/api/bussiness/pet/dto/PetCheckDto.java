package kr.co.petmates.api.bussiness.pet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetCheckDto {
    private Long id;
    private String name;
    private String photoUrl; // /uploads/ + storedFileName으로 구성됩니다.

//    public static PetDto toPetDto(Pet pet) {
//        PetDto petDto = new PetDto();
//        petDto.setId(pet.getId());
//        petDto.setName(pet.getName());
//        // PetPhoto 정보 설정
//        if (pet.getPetPhoto() != null) {
//            petDto.setStoredFileName(pet.getPetPhoto().getStoredFileName());
//        }
//        return petDto;
//    }
}
