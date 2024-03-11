package kr.co.petmates.api.bussiness.pet.controller;


import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import kr.co.petmates.api.bussiness.pet.dto.PetDto;
import kr.co.petmates.api.bussiness.pet.dto.PetResponseDto;
import kr.co.petmates.api.bussiness.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    // 펫 정보 입력 및 유효성 검증
    @PostMapping("/my-page/pet/add")
    public ResponseEntity<?> add(@Valid @RequestBody PetDto petDto, BindingResult bindingResult) {

        // petDto를 Service로 전달
        return petService.add(petDto, bindingResult);
    }

    // 펫 정보 조회
    @GetMapping("/my-page/petsitter/{petId}")
    public ResponseEntity<?> getPet(@PathVariable("petId") Long petId) {
        try {
            PetDto petDto = petService.findPetById(petId);
            return ResponseEntity.ok(petDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("/petsitter/select-pet")
//    public ResponseEntity<?> selectPet(Long memberId) {
//        try {
//            PetDto pet = petService.findPetByMembersId(memberId);
//            if (pet != null) {
//                Map<String, Object> sucessRes = new HashMap<>();
//                sucessRes.put("result", "success");
//                sucessRes.put("data", pet);
//
//                return ResponseEntity.ok(sucessRes);
//            } else {
//                Map<String, Object> errorRes = new HashMap<>();
//                errorRes.put("result", "fail");
//                errorRes.put("data", Collections.singletonMap("reason", "게시글이 존재하지 않습니다."));
//
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRes);
//            }
//        } catch (Exception e) {
//            Map<String, Object> errorRes = new HashMap<>();
//            errorRes.put("result", "fail");
//            errorRes.put("data", Collections.singletonMap("reason", "에러가 발생했습니다."));
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
//        }
//    }

    @GetMapping("my-page/pet/{membersId}")
    public ResponseEntity<?> myPetList(@PathVariable("membersId") Long membersId) {
        try {
            List<PetResponseDto> pets = petService.findPetsByOwnerId(membersId);
            return ResponseEntity.ok(Map.of("result", "success", "data", pets));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(Map.of("result", "failed",
                    "data", Map.of("reason", e.getMessage())));
            // 400 에러
//            return ResponseEntity.badRequest().body(Map.of("result", "failed",
//                    "data", Map.of("reason", e.getMessage())));
        }
    }
}
