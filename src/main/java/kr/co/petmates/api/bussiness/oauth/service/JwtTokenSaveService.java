package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenSaveService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);

    public static void saveTokenToCookies(HttpServletResponse response, UserService.AuthResult authResult) {
        String jwtToken = authResult.getJwtToken();
        // JWT 토큰을 세션 쿠키에 저장
        Cookie jwtTokenCookie = new Cookie("jwtToken", jwtToken);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setMaxAge(-1); // 세션 쿠키로 설정하여 브라우저 종료 시 삭제
        response.addCookie(jwtTokenCookie);
        logger.info("JWT 토큰 쿠키 저장: {}", jwtTokenCookie);

        String refreshToken = authResult.getRefreshToken();
        // 리프레시 토큰을 세션 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(-1); // 세션 쿠키로 설정하여 브라우저 종료 시 삭제
        response.addCookie(refreshTokenCookie);
        logger.info("리프레시 토큰 쿠키 저장: {}", refreshTokenCookie);
    }

    public static void deleteTokenToCookies(HttpServletResponse response) {
        // JWT 토큰이 저장된 쿠키 삭제
        Cookie jwtTokenCookie = new Cookie("jwtToken", null); // 쿠키 이름에 해당하는 새 쿠키 생성
        jwtTokenCookie.setHttpOnly(true); // HttpOnly 설정 유지
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 즉시 만료
        response.addCookie(jwtTokenCookie); // 응답에 쿠키 추가하여 클라이언트에게 전송
        logger.info("컨테이너 로그아웃 jwtTokenCookie 삭제?: {}", jwtTokenCookie);

        // refresh 토큰이 저장된 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null); // 쿠키 이름에 해당하는 새 쿠키 생성
        refreshTokenCookie.setHttpOnly(true); // HttpOnly 설정 유지
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 즉시 만료
        response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가하여 클라이언트에게 전송
    }
}
