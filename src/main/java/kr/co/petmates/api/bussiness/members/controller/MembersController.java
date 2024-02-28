package kr.co.petmates.api.bussiness.members.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.members.dto.MembersDTO;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.members.service.MembersService;
import kr.co.petmates.api.bussiness.oauth.client.KakaoApiClient;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.service.JwtTokenSaveService;
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
    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenSaveService jwtTokenSaveService;

    // 기본 정보 보여주기
    @GetMapping("/join")
    public ResponseEntity<MembersDTO> getMemberInfo(HttpSession session) {
        logger.info("회원가입(get) 호출 성공");

        // 세션에서 사용자 정보 가져오기
        MembersDTO tempUserInfo = (MembersDTO) session.getAttribute("tempUserInfo");

        if (tempUserInfo != null) {
            logger.info("세션에서 정보 가져오기");
            logger.info("세션 정보 이메일:{}", tempUserInfo.getEmail());
            logger.info("세션 정보 닉네임:{}", tempUserInfo.getNickname());
            // 세션에 사용자 정보가 있으면, 이 정보를 JSON 형태로 클라이언트에 반환합니다.
            return ResponseEntity.ok(tempUserInfo);
        } else {
            // 세션에 사용자 정보가 없으면, 빈 MembersDTO 객체를 생성하여 이를 반환합니다.
            // 상태 코드는 204 No Content가 적절할 수 있으나, 클라이언트에서 null 체크를 하므로,
            // 여기서는 빈 객체를 반환하고 상태 코드는 200 OK를 사용합니다.
            return ResponseEntity.ok(new MembersDTO());
        }
    }

    @PostMapping("/join/save")
    public ResponseEntity<?> saveMember(@RequestBody MembersDTO membersDTOFromClient, HttpSession session) {
        // 세션에서 임시 저장된 MembersDTO 정보 가져오기
        MembersDTO sessionMembersDTO = (MembersDTO) session.getAttribute("tempUserInfo");

        if (sessionMembersDTO == null) {
            // 세션에 정보가 없으면, 에러 메시지 반환
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("result", "error");
            responseBody.put("data", "세션에 사용자 정보가 없습니다.");
            return ResponseEntity.badRequest().body(responseBody);
        }

        // 세션 DTO 정보와 프론트에서 받은 DTO 정보 결합
        // 여기서는 프론트에서 변경 가능한 정보만 업데이트
        sessionMembersDTO.setPhone(membersDTOFromClient.getPhone());
        sessionMembersDTO.setZipcode(membersDTOFromClient.getZipcode());
        sessionMembersDTO.setFullAddr(membersDTOFromClient.getFullAddr());
        sessionMembersDTO.setRoadAddr(membersDTOFromClient.getRoadAddr());
        sessionMembersDTO.setDetailAddr(membersDTOFromClient.getDetailAddr());
        sessionMembersDTO.setLatitude(membersDTOFromClient.getLatitude());
        sessionMembersDTO.setLongitude(membersDTOFromClient.getLongitude());

        // MembersDTO를 Members 엔티티로 변환
        Members member = Members.toMembersEntity(sessionMembersDTO);
        // Members 엔티티 저장
        membersRepository.save(member);

        // 세션에서 사용자 정보 제거
        session.removeAttribute("tempUserInfo");

        // 성공 응답 반환
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

    @PostMapping("/delete")
    public ResponseEntity<?> deleteMember(@RequestBody Map<String, String> payload, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String inputKakaoAccount = payload.get("kakaoAccount");
        logger.info("회원탈퇴 클라이언트 이메일: {}", inputKakaoAccount);

        String jwtToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    logger.info("회원가입(delete) 쿠키에서 찾은 jwtToken: {}", jwtToken);
                    break;
                }
            }
        }
        // jwtToken 에 포함된 사용자 이메일 찾기
        String email = jwtTokenProvider.getEmail(jwtToken);

        // 입력받은 카카오 계정과 토큰에서 추출한 이메일 비교
        if (!inputKakaoAccount.equals(email)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("result", "failed");
            responseBody.put("data","이메일을 다시 입력해주세요.");
            return ResponseEntity.ok(responseBody);
        }

        // 1. 카카오 연결끊기 API 2. 카카오 로그아웃 API
        boolean isKakaoLogout = kakaoApiClient.kakaoUnlink(session);
        logger.info("로그아웃 성공 여부: {}", isKakaoLogout);

        // 3. 데이터베이스 사용자 정보 삭제

        // 해당 사용자의 정보 삭제
        Members member = membersRepository.findByEmail(email).orElse(null);
        membersRepository.delete(member);

        // 4. 세션 초기화 및 쿠키 삭제
        if (isKakaoLogout) {   // 카카오로그아웃 처리 성공
            session.invalidate();
            jwtTokenSaveService.deleteTokenToCookies(response);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("result", "success");
            responseBody.put("data","회원탈퇴가 완료되었습니다.");
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("result", "failed");
            responseBody.put("data","회원탈퇴 처리 중 오류가 발생했습니다.");
            return ResponseEntity.ok(responseBody);
//            return ResponseEntity.badRequest().body(responseBody);
        }
    }
}