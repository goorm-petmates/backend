// 사용자정보로 데이터베이스 저장, isNewUse 생성, 최종 반환값
package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserCheckService userCheckService;
    @Autowired
    private JwtTokenSaveService jwtTokenSaveService;

    // 카카오서버로부터 전달받은 사용자정보 데이터베이스 저장 / isNewUser, jwtToken, refreshToken 생성 후 반환
    public AuthResult createUserResult(KakaoUserInfoResponse userInfo, HttpServletResponse response) {
        String email = userInfo.getEmail(); // 프로필에서 이메일 정보 추출
        logger.info("userService email값: {}", email);

        // isNewUser 값 반환, 이메일로 데이터베이스 조회
        boolean isNewUser = userCheckService.isNewUser(email);
        logger.info("userService isNewUser값: {}", isNewUser);

        String jwtToken = jwtTokenProvider.createJwtToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken(jwtToken);
        // 생성한 토큰을 쿠키에 저장
        jwtTokenSaveService.saveTokenToCookies(jwtToken, refreshToken, response);

        // 사용자 저장 또는 업데이트
        userCheckService.saveOrUpdateUser(userInfo);
        return new AuthResult(jwtToken, refreshToken, isNewUser);
    }

    // static 내부 클래스로 AuthResult 정의
    public static class AuthResult extends Members {
        private final String jwtToken;
        private final String refreshToken;
        private final boolean isNewUser;

        public AuthResult(String jwtToken, String refreshToken, boolean isNewUser) {
            this.jwtToken = jwtToken;
            this.refreshToken = refreshToken;
            this.isNewUser = isNewUser;
        }

        public String getJwtToken() {
            return jwtToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public boolean isNewUser() {
            return isNewUser;
        }

    }
}