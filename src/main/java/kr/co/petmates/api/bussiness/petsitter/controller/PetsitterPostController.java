package kr.co.petmates.api.bussiness.petsitter.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import kr.co.petmates.api.bussiness.petsitter.dto.PetsitterDto;
import kr.co.petmates.api.bussiness.petsitter.dto.PetsitterDto.PetsitterProjection;
import kr.co.petmates.api.bussiness.petsitter.service.PetsitterPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PetsitterPostController {
    private final PetsitterPostService petsitterPostService;

    // 펫시터 게시글의 존재 여부 및 Petsitter ID 확인
    @GetMapping("/my-page/petsitter/existence/{membersId}")
    public ResponseEntity<?> checkPetsitterPostAndReturnId(@PathVariable("membersId") Long membersId) {
        return petsitterPostService.findPetsitterIdByMembersId(membersId)
                .map(petsitterId -> {
                    // petsitterId를 사용하여 필요한 추가 데이터를 검색

                    Optional<PetsitterDto> content = petsitterPostService.getContentsByPetsitterId(petsitterId);
                    return ResponseEntity.ok()
                            .body(Map.of("exists", true,
                                    "petsitter_id", petsitterId,
                                    "data", content));
                })
                .orElseGet(() -> ResponseEntity.ok().body(Map.of(
                        "exists", false,
                        "applyUrl", "/api/petsitter/apply"))); // "등록하기" 버튼을 위한 URL 추가
    }
    // 펫시터 게시글 리스트 조회
    @GetMapping("/petsitter/list")
    public ResponseEntity<List<PetsitterProjection>> getAllPetsittersWithMemberId() {
        List<PetsitterProjection> petsitters = petsitterPostService.findAllPetsittersWithMemberId();
        return ResponseEntity.ok(petsitters);
    }
}
