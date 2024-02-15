package kr.co.petmates.api.bussiness.members.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import kr.co.petmates.api.bussiness.members.dto.MembersDTO;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.members.service.MembersService;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
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
@RequestMapping("/api/members")
public class MembersController {
    private static final Logger logger = LoggerFactory.getLogger(MembersController.class);

    private final MembersService membersService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MembersRepository membersRepository;

    // 기본 정보 보여주기
    @GetMapping("/join")
    public ResponseEntity<Members> getMemberInfo(HttpServletRequest request) {
        logger.info("회원가입(get) 호출 성공");
        // 1. 쿠키에서 jwtToken 찾기
        String jwtToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    logger.info("회원가입(get) 쿠키에서 찾은 jwtToken: {}", jwtToken);
                }
            }
        }

        // 2. jwtToken 에 포함된 사용자 이메일 찾기
        String email = jwtTokenProvider.getEmail(jwtToken);

        // 3. 해당 사용자의 정보 불러오기
        Members member = membersRepository.findByEmail(email).orElse(null);;
        if (member.getEmail() != null) {
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 추가 정보 저장
    @PostMapping("/join/save")
    public ResponseEntity<?> saveMember(@RequestBody MembersDTO membersDTO) {
        Optional<Members> existingMember = membersRepository.findByEmail(membersDTO.getEmail());

        if (!existingMember.isPresent()) {
            // 회원이 존재하지 않는 경우, 에러 처리 또는 새로운 회원 등록 로직을 구현
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("result", "failed");
            responseBody.put("data", "이메일이 유효하지 않습니다. 카카오로그인부터 다시 시도해주세요.");
            return ResponseEntity.ok().body(responseBody);
        }

        Members member = existingMember.get();
        logger.info("회원가입(save) 전달받은 이메일: {}", membersDTO.getEmail());
        logger.info("회원가입(save) 전달받은 닉네임: {}", membersDTO.getNickname());

        if (membersDTO.getNickname() != null) {
            member.setNickname(membersDTO.getNickname());
        }

        member.setPhone(membersDTO.getPhone());
        member.setRoadAddr(membersDTO.getRoadAddr());
        member.setDetailAddr(membersDTO.getDetailAddr());
        member.setLatitude(membersDTO.getLatitude());
        member.setLongitude(membersDTO.getLongitude());
        member.setZipcode(membersDTO.getZipcode());

        membersRepository.save(member);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("result", "success");
        responseBody.put("data", "회원가입에 성공했습니다 :)");
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/join/doublecheck")
    public ResponseEntity<?> doubleCheckNickname(@RequestBody String nickname) {
        boolean isNicknameDuplicate = membersRepository.findByNickname(nickname).isPresent();

        if (isNicknameDuplicate) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("result", "failed");
            return ResponseEntity.ok().body(responseBody);
        } else {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("result", "success");
            return ResponseEntity.ok().body(responseBody);
        }
    }
}