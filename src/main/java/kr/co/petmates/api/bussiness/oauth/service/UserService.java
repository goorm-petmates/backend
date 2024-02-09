// 사용자정보로 데이터베이스 조회, isNewUse 체크
package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.oauth.client.KakaoApiClient;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import kr.co.petmates.api.bussiness.oauth.domain.User;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.bussiness.oauth.repository.UserRepository;
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
    private KakaoApiClient kakaoApiClient;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private UserCheckService userCheckService;

    @Autowired // 이 어노테이션은 생성자 기반 의존성 주입을 나타냅니다. Spring이 자동으로 UserRepository와 KakaoApiClient의 인스턴스를 주입합니다.
    public UserService(UserRepository userRepository, KakaoApiClient kakaoApiClient) {
        this.userRepository = userRepository;
        this.kakaoApiClient = kakaoApiClient;
    }

    // 카카오서버로부터 전달받은 사용자정보 데이터베이스 저장 / isNewUser, jwtToken, refreshToken 생성 후 반환
    public AuthResult createUserResult(KakaoUserInfoResponse userInfo, String accessToken) {
        logger.info("userService 넘겨받은 사용자정보: {}", userInfo);

        String email = userInfo.getEmail(); // 프로필에서 이메일 정보 추출
        logger.info("userService email값: {}", email);
        String nickname = userInfo.getNickname(); // 프로필에서 이메일 정보 추출
        logger.info("userService nickname값: {}", nickname);

        // isNewUser 값 반환, 이메일로 데이터베이스 조회
        boolean isNewUser = userCheckService.isNewUser(email);
        logger.info("userService isNewUser값: {}", isNewUser);

        String jwtToken = jwtTokenProvider.createJwtToken(email, accessToken);
        String refreshToken = jwtTokenProvider.createRefreshToken(jwtToken);

        // UserCheckService를 사용하여 사용자 저장 또는 업데이트
        userCheckService.saveOrUpdateUser(userInfo, refreshToken);

        logger.info("userService jwtToken값: {}", jwtToken);
        return new AuthResult(jwtToken, refreshToken, isNewUser);
    }

    // static 내부 클래스로 AuthResult 정의
    public static class AuthResult extends User {
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