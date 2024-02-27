package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenSaveService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);

    public static void saveTokenToCookies(String jwtToken, String refreshToken, HttpServletResponse response) {
        // JWT 토큰을 세션 쿠키에 저장
        Cookie jwtTokenCookie = new Cookie("jwtToken", jwtToken);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setMaxAge(-1); // 세션 쿠키로 설정하여 브라우저 종료 시 삭제
        response.addCookie(jwtTokenCookie);
        logger.info("JWT 토큰 쿠키 저장");

        // 리프레시 토큰을 세션 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(-1); // 세션 쿠키로 설정하여 브라우저 종료 시 삭제
        response.addCookie(refreshTokenCookie);
//        logger.info("리프레시 토큰 쿠키 저장: {}", refreshTokenCookie);
    }

    public static void deleteTokenToCookies(HttpServletResponse response) {
        // JWT 토큰이 저장된 쿠키 삭제
        Cookie jwtTokenCookie = new Cookie("jwtToken", null);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 즉시 만료
        response.addCookie(jwtTokenCookie);

        // refresh 토큰이 저장된 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 즉시 만료
        response.addCookie(refreshTokenCookie);
    }

    public static String findTokenToCookies(HttpServletRequest request) {       // 블랙리스트 또는 레디스 사용할 때
        String findToken = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) { // 쿠키 배열이 null이 아닐 경우에만 처리
            for (Cookie cookie : cookies) { // 쿠키 배열을 순회하며
                if ("jwtToken".equals(cookie.getName())) { // 쿠키 이름이 "jwtToken"인 경우
                    findToken = cookie.getValue(); // 해당 쿠키의 값을 jwtToken 변수에 저장
                    break; // 찾았으면 더 이상의 순회는 필요 없으므로 반복문 탈출
                } else if ("refreshToken".equals(cookie.getName())) { // "refreshToken"
                    findToken = cookie.getValue();
                }
            }
        }
        return findToken;
    }
}
