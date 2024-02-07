package kr.co.petmates.api.bussiness.pet.service;

import static kr.co.petmates.api.bussiness.pet.entity.Pet.toPetEntity;
import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.pet.dto.PetDto;
import kr.co.petmates.api.bussiness.pet.entity.Pet;
import kr.co.petmates.api.bussiness.pet.repository.PetRepository;
import kr.co.petmates.api.enums.UserInterfaceMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;


    // 펫 정보 등록
    public ResponseEntity<?> add(PetDto petDto, Members members, BindingResult bindingResult) {

        // 유효성 검사 실패 처리
        if (bindingResult.hasErrors()) {
            return validationErrors(bindingResult);
        }

        Pet pet = toPetEntity(petDto); // PetDto를 Pet 엔티티로 변환
        pet.setOwner(members); // 사용자 정보와 Pet 엔티티 연결

        petRepository.save(pet);
        return ResponseEntity.ok().build(); // 성공적인 저장
    }

    // 유효성 검사 처리
    public ResponseEntity<?> validationErrors(BindingResult bindingResult) {
        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String errorMessage = switch (fieldError.getCode()) {
                case "NotNull" -> UserInterfaceMsg.ERR_NOT_REQUIRED_FIELD.getValue();
                case "Pattern" -> UserInterfaceMsg.ERR_SAVE_INPUT_TYPE.getValue();
                case null -> null;
                default -> "유효성 검사 오류";
            };

            fieldErrors.put(fieldError.getField(), errorMessage);
        }
        errorResponse.put("errors", fieldErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 펫 정보 및 사진 조회
    public PetDto findPetById(Long petId) {
        return petRepository.findById(petId).map(pet -> {
            PetDto petDto = PetDto.toPetDto(pet);
            if (pet.getPetPhoto() != null) {
                String photoUrl = "/uploads/" + pet.getPetPhoto().getStoredFileName();
                petDto.setPhotoUrl(photoUrl);
            }
            return petDto;
        }).orElseThrow(() -> new RuntimeException(UserInterfaceMsg.ERR_NOT_EXIST_PET.getValue()));
    }
}


