package kr.co.petmates.api.bussiness.pet.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.co.petmates.api.bussiness.pet.entity.Pet;
import kr.co.petmates.api.bussiness.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/petsitter")
@RequiredArgsConstructor
public class PetInfoCheckController {
    private final PetService petService;

    // 반려동물 정보 체크
    @GetMapping("/select-pet")
    public ResponseEntity<?> getPets(HttpServletRequest request) {
        List<Pet> pets = petService.findPetsByUserEmail(request);
        Map<String, Object> response = new HashMap<>();

        if (pets.isEmpty()) {
            response.put("result", "failed");
            response.put("data", "등록된 펫 정보 없음");
        } else {
            response.put("result", "success");
            response.put("data", pets);
        }

        return ResponseEntity.ok(response);
    }
}
