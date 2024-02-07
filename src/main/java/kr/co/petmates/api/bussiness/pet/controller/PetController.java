package kr.co.petmates.api.bussiness.pet.controller;


import jakarta.validation.Valid;
import kr.co.petmates.api.bussiness.pet.dto.PetDto;
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
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    // 펫 정보 입력 및 유효성 검증
    @PostMapping("/pet/add")
    public ResponseEntity<?> add(@Valid @RequestBody PetDto petDto, BindingResult bindingResult) {

        // petDto를 Service로 전달
        return petService.add(petDto, bindingResult);
    }

    // 펫 정보 조회
    @GetMapping("/petsitter/{petId}")
    public ResponseEntity<?> getPet(@PathVariable("petId") Long petId) {
        try {
            PetDto petDto = petService.findPetById(petId);
            return ResponseEntity.ok(petDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
