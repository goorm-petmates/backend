package kr.co.petmates.api.bussiness.members.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.members.dto.MembersDTO;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.members.service.MembersService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/my-page")
public class MypageInfoController {
    private static final Logger logger = LoggerFactory.getLogger(MembersController.class);

    private final MembersService membersService;
    private final MembersRepository membersRepository;

    // 내정보 보여주기
    @GetMapping("/check")
    public ResponseEntity<Members> checkMyInfo(HttpServletRequest request) {
        logger.info("내정보(get) 호출 성공");
        Members member = membersService.getMemberInfoFromJwtToken(request);

        if (member != null && member.getEmail() != null) {
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editMyInfo(@RequestBody MembersDTO membersDTO) {
        membersService.editMemberInfo(membersDTO);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("result", "success");
        responseBody.put("data", "회원정보가 수정되었습니다.");
        return ResponseEntity.ok().body(responseBody);
    }
}
