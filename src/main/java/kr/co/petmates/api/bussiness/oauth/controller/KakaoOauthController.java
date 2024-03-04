
package kr.co.petmates.api.bussiness.oauth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.oauth.client.KakaoApiClient;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.bussiness.oauth.service.AccessTokenStorage;
import kr.co.petmates.api.bussiness.oauth.service.JwtTokenSaveService;
import kr.co.petmates.api.bussiness.oauth.service.KakaoOauthService;
import kr.co.petmates.api.bussiness.oauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoOauthController {

    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    private final KakaoOauthService kakaoOauthService;
    private final KakaoApiClient kakaoApiClient;
    private final AccessTokenStorage accessTokenStorage;
    private final UserService userService;
    private final JwtTokenSaveService jwtTokenSaveService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MembersRepository membersRepository;

    @PostMapping("/login")   // 로그인 요청
    public ResponseEntity<?> kakaoLogin(HttpServletResponse response, HttpSession session, @RequestBody Map<String, String> requestBody) {
        // 인가코드 추출
        String code = requestBody.get("code");
        if (code == null) {
            return ResponseEntity.badRequest().body("인가코드 없음");
        }
        logger.info("인가코드 추출: {}", code);

        // 인가코드로 카카오 엑세스토큰 요청
        kakaoOauthService.getAccessToken(session, code);

        // 액세스 토큰을 사용하여 사용자 정보를 요청하고 결과를 가져옵니다.
        KakaoUserInfoResponse userInfo = kakaoOauthService.getUserInfo(session);
        logger.info("컨테이너 사용자정보: {}", userInfo);

        // 클라이언트에 전달할 isNewUser 생성, 사용자정보 데이터베이스 저장, jwt토큰과 리프레시 토큰 생성 및 저장
        UserService.AuthResult authResult = userService.createUserResult(userInfo, response, session);

        // isNewUser 정보는 응답 본문에 포함
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isNewUser", authResult.isNewUser());
        logger.info("컨테이너 isNewUser: {}", authResult.isNewUser());
        return ResponseEntity.ok(responseBody);
    }

    // 쿠키에 jwtToken 저장여부 판단 -> 로그인/로그아웃 체크
    @GetMapping("/login/status")
    public ResponseEntity<?> checkAuthStatus(HttpServletRequest request) {
        boolean isLoggedIn = checkLoginStatus(request);
        logger.info("컨테이너 로그인 상태 체크 isLoggedIn: {}", isLoggedIn);

        return ResponseEntity.ok().body(Collections.singletonMap("isLoggedIn", isLoggedIn));
    }
    private boolean checkLoginStatus(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {    // "jwtToken" 쿠키를 찾아 값이 비어있지 않은지 확인
                if ("jwtToken".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    logger.info("컨테이너 쿠키 JwtToken 있음");
                    String jwtToken = cookie.getValue();
                    String email = jwtTokenProvider.getEmail(jwtToken);
                    Optional<Members> memberOptional = membersRepository.findByEmail(email);
                    if (memberOptional.isPresent()) {
                        Members member = memberOptional.get();
                        boolean isNewUser = (member.getPhone() != null);
                        logger.info("컨테이너 로그인 상태 isNewUser: {}", isNewUser);
                        // phone 데이터가 null이면 어떤 경우라도 true 반환
                        return member.getPhone() != null;
                    }
                }
            }
        }
        logger.info("컨테이너 쿠키 JwtToken 없음");
        return false;
    }

    // 로그아웃 요청
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, HttpSession session) {
        boolean isKakaoLogout = kakaoApiClient.kakaoLogout(session);
        logger.info("로그아웃 성공 여부: {}", isKakaoLogout);

        if (isKakaoLogout) {   // 카카오로그아웃 처리 성공
            session.invalidate();
            jwtTokenSaveService.deleteTokenToCookies(response);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("result", "success");
            responseBody.put("data","로그아웃 성공");
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("result", "failed");
            responseBody.put("data","카카오 엑세스토큰 만료");
            return ResponseEntity.ok(responseBody);
//            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    @PostMapping("/logout/update")   // /logout 에서 failed 응답일 때, 엑세스토큰 갱신 요청
    public ResponseEntity<?> logoutUpdateKakaoToken(HttpServletResponse response, HttpSession session) {
        String accessToken = accessTokenStorage.getAccessToken(session);
        logger.info("로그아웃 갱신 전 엑세스토큰: {}", accessToken);
        String refreshToken = accessTokenStorage.getRefreshToken(session);
        logger.info("로그아웃 갱신 전 리프레시토큰: {}", refreshToken);
        kakaoApiClient.updateAccessToken(session, refreshToken);   // 카카오서버에 엑세스토큰 갱신 요청 후, 갱신된 엑세스토큰 세션 저장

        boolean isKakaoLogout = kakaoApiClient.kakaoLogout(session);

        if (isKakaoLogout) {
            session.invalidate();   // 세션에 저장된 엑세스토큰 초기화
            jwtTokenSaveService.deleteTokenToCookies(response);   // 쿠키에 저장된 jwtToken 삭제

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("result", "success");
            logger.info("로그아웃 카카오토큰 갱신 return: {}", ResponseEntity.ok(responseBody));
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("result", "failed");
            responseBody.put("data","로그아웃 처리 안됨");
            return ResponseEntity.ok(responseBody);
//            return ResponseEntity.badRequest().body(responseBody);
        }
    }
}