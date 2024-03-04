package kr.co.petmates.api.bussiness.pet.service;

import static kr.co.petmates.api.bussiness.pet.entity.Pet.toPetEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.service.MembersService;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final MembersService membersService;


    // 펫 정보 등록
    public ResponseEntity<?> add(PetDto petDto, BindingResult bindingResult) {

        // 유효성 검사 실패 처리
        if (bindingResult.hasErrors()) {
            return validationErrors(bindingResult);
        }

        Pet pet = toPetEntity(petDto); // PetDto를 Pet 엔티티로 변환

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
    // 반려동물 정보 체크
    public List<Pet> findPetsByUserEmail(HttpServletRequest request) {
        // JWT 토큰에서 회원 정보 조회
        Members member = membersService.getMemberInfoFromJwtToken(request);
        if (member == null) {
            // 회원 정보가 없는 경우 빈 리스트 반환
            return Collections.emptyList();
        }
        // 회원 ID를 기반으로 펫 정보 조회
        return petRepository.findByOwner_Id(member.getId());
    }
//    public PetDto findPetByMembersId(Long memberId) {
//        List<Pet> pets = petRepository.findByOwnerId(memberId);
//
//        return null;
//    }
}


