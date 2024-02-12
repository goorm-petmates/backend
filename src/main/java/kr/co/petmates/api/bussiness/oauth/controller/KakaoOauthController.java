// KakaoOauthController는 사용자의 인가 코드를 처리하고, JWT 토큰을 생성하여 반환하는 컨트롤러입니다.
package kr.co.petmates.api.bussiness.oauth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.oauth.client.KakaoApiClient;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.bussiness.oauth.service.AccessTokenStorage;
import kr.co.petmates.api.bussiness.oauth.service.KakaoOauthService;
import kr.co.petmates.api.bussiness.oauth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 이 클래스를 REST 컨트롤러로 선언합니다.
@RequestMapping("/api/kakao") // 이 컨트롤러의 모든 매핑은 "/api/kakao" 경로로 시작합니다.
public class KakaoOauthController {

    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    @Autowired // 스프링의 의존성 주입 기능을 사용하여 KakaoOauthService 객체를 자동으로 주입합니다.
    private KakaoOauthService kakaoOauthService;
    @Autowired
    private KakaoApiClient kakaoApiClient;
    @Autowired
    private AccessTokenStorage accessTokenStorage;
    @Autowired // UserService 객체를 자동으로 주입합니다.
    private UserService userService;

    @PostMapping("/login") // "/login" 경로로 POST 요청이 오면 이 메소드를 실행합니다.
    public ResponseEntity<?> kakaoLogin(HttpServletResponse response, HttpSession session, @RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        if (code == null) { // 인가코드 null 인 경우
            return ResponseEntity.badRequest().body("Authorization code is missing");
        }
        logger.info("인가코드: {}", code);

        // 인가 코드를 사용하여 액세스 토큰을 요청 및 세션 저장합니다.
        String accessToken = kakaoOauthService.getAccessToken(session, code);
        logger.info("컨테이너 새로 생성된 엑세스토큰: {}", accessToken);

        // 액세스 토큰을 사용하여 사용자 정보를 요청하고 결과를 가져옵니다.
        KakaoUserInfoResponse userInfo = kakaoOauthService.getUserInfo(session);
        logger.info("컨테이너 사용자정보: {}", userInfo);

        // 사용자정보로 jwt 토큰, refresh 토큰 생성, 사용자정보 저장 요청
//        UserService.AuthResult authResult = userService.createUserResult(userInfo);
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("jwtToken", authResult.getJwtToken());
//        responseBody.put("refreshToken", authResult.getRefreshToken());
//        responseBody.put("isNewUser", authResult.isNewUser());


        // 사용자정보로 jwt 토큰, refresh 토큰 생성, 사용자정보 저장 요청
        UserService.AuthResult authResult = userService.createUserResult(userInfo);

        // JWT 토큰을 세션 쿠키에 저장
        Cookie jwtTokenCookie = new Cookie("jwtToken", authResult.getJwtToken());
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setMaxAge(-1); // 세션 쿠키로 설정하여 브라우저 종료 시 삭제
        response.addCookie(jwtTokenCookie);
        logger.info("컨테이너 로그인 jwtTokenCookie: {}", jwtTokenCookie);

        // 리프레시 토큰을 세션 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", authResult.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(-1); // 세션 쿠키로 설정하여 브라우저 종료 시 삭제
        response.addCookie(refreshTokenCookie);

        // isNewUser 정보는 응답 본문에 포함
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isNewUser", authResult.isNewUser());
        logger.info("컨테이너 isNewUser: {}", authResult.isNewUser());
        logger.info("컨테이너 return: {}", ResponseEntity.ok(responseBody));
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/login/status")
    public ResponseEntity<?> checkAuthStatus(HttpServletRequest request) {
        // 쿠키에서 토큰을 검사하고 로그인 상태 확인 로직 구현
        boolean isLoggedIn = checkLoginStatus(request);
        logger.info("컨테이너 로그인 상태 체크 isLoggedIn: {}", isLoggedIn);
        // 로그인 상태를 클라이언트에게 반환
        return ResponseEntity.ok().body(Collections.singletonMap("isLoggedIn", isLoggedIn));
    }

    private boolean checkLoginStatus(HttpServletRequest request) {
        // 쿠키에서 토큰 존재 여부 확인, 토큰의 유효성 검증 등을 수행
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {    // "jwtToken" 쿠키를 찾아 값이 비어있지 않은지 확인
                if ("jwtToken".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    // 쿠키에 "jwtToken"이 존재하고 값이 비어있지 않으면 로그인 상태로 간주
                    logger.info("컨테이너 쿠키 JwtToken 있음");
                    return true;
                }
            }
        }
        // "jwtToken" 쿠키가 없거나 값이 비어있으면 로그인 상태가 아님
        logger.info("컨테이너 쿠키 JwtToken 없음");
        return false;
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, HttpSession session) {

        boolean isKakaoLogout = kakaoApiClient.kakaoLogout(session);
        logger.info("로그아웃 성공 여부: {}", isKakaoLogout);

        if (isKakaoLogout) {
            session.invalidate();

            // JWT 토큰이 저장된 쿠키 삭제
            Cookie jwtTokenCookie = new Cookie("jwtToken", null); // 쿠키 이름에 해당하는 새 쿠키 생성
            jwtTokenCookie.setPath("/");
            jwtTokenCookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 즉시 만료
            jwtTokenCookie.setHttpOnly(true); // HttpOnly 설정 유지
            response.addCookie(jwtTokenCookie); // 응답에 쿠키 추가하여 클라이언트에게 전송
            logger.info("컨테이너 로그아웃 jwtTokenCookie 삭제?: {}", jwtTokenCookie);
            // refresh 토큰이 저장된 쿠키 삭제
            Cookie refreshTokenCookie = new Cookie("refreshToken", null); // 쿠키 이름에 해당하는 새 쿠키 생성
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 즉시 만료
            refreshTokenCookie.setHttpOnly(true); // HttpOnly 설정 유지
            response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가하여 클라이언트에게 전송

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("result", "success");
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("result", "failed");
            responseBody.put("data","카카오 엑세스토큰 만료");
//            return ResponseEntity.ok(responseBody);
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    @PostMapping("/logout/update")
    public ResponseEntity<?> logoutUpdateKakaoToken(HttpSession session) {
        String accessToken = accessTokenStorage.getAccessToken(session);
        logger.info("로그아웃 갱신 전 엑세스토큰: {}", accessToken);
        String refreshToken = accessTokenStorage.getRefreshToken(session);
        logger.info("로그아웃 갱신 전 리프레시토큰: {}", refreshToken);
        kakaoApiClient.updateAccessToken(session, refreshToken);

        kakaoApiClient.kakaoLogout(session);

        session.invalidate();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "success");
        logger.info("로그아웃 카카오토큰 갱신 return: {}", ResponseEntity.ok(responseBody));
        return ResponseEntity.ok(responseBody);
    }
}