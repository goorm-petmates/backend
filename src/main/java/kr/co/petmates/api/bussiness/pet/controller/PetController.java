package kr.co.petmates.api.bussiness.pet.controller;


import jakarta.validation.Valid;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.pet.dto.PetDto;
import kr.co.petmates.api.bussiness.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/my-page/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    // 펫 정보 입력 및 유효성 검증
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    public ResponseEntity<?> add(@Valid @RequestBody PetDto petDto, BindingResult bindingResult,
                                    Authentication authentication) {
        // Authentication 객체에서 사용자 정보 추출
        Members members = (Members) authentication.getPrincipal();

        // petDto와 사용자 정보를 Service로 전달
        return petService.add(petDto, members, bindingResult);
    }
}
