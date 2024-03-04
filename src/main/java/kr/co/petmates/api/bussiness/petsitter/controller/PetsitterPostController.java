package kr.co.petmates.api.bussiness.petsitter.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kr.co.petmates.api.bussiness.petsitter.dto.PetsitterDto;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.bussiness.petsitter.service.PetsitterPostService;
import kr.co.petmates.api.enums.CareType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/petsitter/list")
    public ResponseEntity<List<PetsitterDto>> getPetsitterList() {
        List<PetsitterDto> petsitterList = petsitterPostService.findPetsittersWithMembers();
        return ResponseEntity.ok(petsitterList);
    }


    // 서비스, 지역1, 지역2 선택 후 검색하는 기능
    @GetMapping("/petsitter/search")
    public ResponseEntity<?> getSearch(
            @RequestParam(name = "careType") CareType careType,
            @RequestParam(name = "area1") String area1,
            @RequestParam(name = "area2") String area2
            ) {
        try {
            List<PetsitterDto> search = petsitterPostService.findSearch(careType, area1, area2);

            // 성공 응답
            Map<String, Object> sucessRes = new HashMap<>();
            sucessRes.put("result", "success");
            sucessRes.put("data", search);

            return ResponseEntity.ok(sucessRes);
        } catch (Exception e) {
            // 예외 처리 후 에러 응답 구성
            Map<String, Object> errorRes = new HashMap<>();
            errorRes.put("result", "fail");
            errorRes.put("data", Collections.singletonMap("reason", "에러가 발생했습니다."));

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
        }


    }

    // 펫시터 지원 게시글 보는 기능
    @GetMapping("/petsitter/list/{id}")
    public ResponseEntity<?> viewPetsitter(
            @PathVariable(name = "id") Long id
            ) {
        try {
            Optional<PetsitterDto> petsitter = petsitterPostService.getPetsitterById(id);
            if (petsitter != null) {
                Map<String, Object> sucessRes = new HashMap<>();
                sucessRes.put("result", "success");
                sucessRes.put("data", petsitter);

                return ResponseEntity.ok(sucessRes);
            } else {
                Map<String, Object> errorRes = new HashMap<>();
                errorRes.put("result", "fail");
                errorRes.put("data", Collections.singletonMap("reason", "게시글이 존재하지 않습니다."));

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRes);
            }
        } catch (Exception e) {
            Map<String, Object> errorRes = new HashMap<>();
            errorRes.put("result", "fail");
            errorRes.put("data", Collections.singletonMap("reason", "에러가 발생했습니다."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
        }
    }

    // 펫시터 지원하기 클릭
//    @GetMapping("/petsitter/apply")
//    public ResponseEntity<?> clickApply() {
//
//    }

    // 펫시터 지원하기
    @PostMapping("/petsitter/apply")
    public ResponseEntity<?> applyAsPetsitter(
            @RequestBody PetsitterDto petsitterDto
    ) {
        try {
            Petsitter petsitter = petsitterPostService.applyAsPetsitter(petsitterDto);

            Map<String, Object> sucessRes = new HashMap<>();
            sucessRes.put("result", "success");
            sucessRes.put("data", petsitter);

            return ResponseEntity.ok(sucessRes);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("result", "failed");
            errorResponse.put("data", Collections.singletonMap("reason", "지원하기에 실패했습니다."));

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/petsitter/list/{id}/promote")
    public ResponseEntity<?> promotePosting(@PathVariable Long id) {
        petsitterPostService.promotePosting(id);
        return ResponseEntity.ok("끌어올리기 성공.");
    }

//    @GetMapping("/petsitter/list")
//    public List<PetsitterListDto> getPetsitterList() {
//        return petsitterPostService.getPetsitterWithMembers();
//    }
}
