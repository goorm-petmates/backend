package kr.co.petmates.api.bussiness.members.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Members> saveMember(@RequestBody Members member) {
        membersRepository.save(member);
        return ResponseEntity.ok().build();
    }
}