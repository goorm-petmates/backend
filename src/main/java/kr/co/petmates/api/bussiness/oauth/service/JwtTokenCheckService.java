package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenCheckService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 및 검증을 담당하는 프로바이더를 주입합니다.
    private final JwtTokenSaveService jwtTokenSaveService;
    private final MembersRepository membersRepository;

    public String checkJwtToken(String jwtToken) {
        boolean isValidateToken = jwtTokenProvider.validateToken(jwtToken);   // jwtToken 유효성 체크
        if(isValidateToken) {
            String email = jwtTokenProvider.getEmail(jwtToken);;
            return email;
        } else {
            return "failed";
        }
    }

    public void saveJwtToken(HttpServletResponse response, String email) {
        String jwtToken = jwtTokenProvider.createJwtToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken(jwtToken);
        // 생성한 토큰을 쿠키에 저장
        jwtTokenSaveService.saveTokenToCookies(jwtToken, refreshToken, response);
    }

    public boolean checkLoginStatus(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {    // "jwtToken" 쿠키를 찾아 값이 비어있지 않은지 확인
                if ("jwtToken".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    logger.info("컨테이너 쿠키 JwtToken 있음");
                    return true;
                }
            }
        }
        logger.info("컨테이너 쿠키 JwtToken 없음");
        return false;
    }
}
