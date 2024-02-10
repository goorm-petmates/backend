package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.servlet.http.HttpSession;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import kr.co.petmates.api.bussiness.oauth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LogoutService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    @Autowired
    private AccessTokenStorage accessTokenStorage; // AccessTokenStorage 추가
    @Value("${kakao.logout}") // application.properties 파일에서 kakao.logout.url 값을 주입받습니다.
    private String kakaoLogoutUrl; // 카카오 로그아웃 API의 URL을 저장하는 변수입니다.

    // 리프레시 토큰 관련 데이터베이스 작업을 처리하는 데 사용되는 리포지토리의 참조를 주입받습니다.
    private final UserRepository userRepository;

    // 생성자를 통해 refreshTokenRepository 의존성을 주입받습니다. 이는 의존성 주입(DI) 원칙을 따릅니다.
    public LogoutService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean logout(HttpSession session) {
        String accessToken = accessTokenStorage.getAccessToken(session);
        kakaoLogout(accessToken); // 이 메소드는 실제 카카오 로그아웃 API를 호출하는 구현이 필요합니다.

        // 세션 무효화
        session.invalidate();
        logger.info("카카오 로그아웃 완료");
        return true;
    }

    public void kakaoLogout(String accessToken) {
        RestTemplate restTemplate = new RestTemplate(); // RESTful API 호출을 위한 RestTemplate 인스턴스를 생성합니다.
        HttpHeaders headers = new HttpHeaders(); // HTTP 요청을 위한 헤더 객체를 생성합니다.
        headers.setBearerAuth(accessToken); // Bearer 토큰을 사용하여 인증 헤더를 설정합니다.
        HttpEntity<String> entity = new HttpEntity<>(headers); // HTTP 요청 본문 없이 헤더만 포함하는 HttpEntity 객체를 생성합니다.

        // 카카오 로그아웃 API를 호출합니다. ResponseEntity 객체를 사용하여 응답을 받습니다.
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoLogoutUrl, HttpMethod.POST, entity, String.class);
    }






//    public void logoutKakao(String accessToken) {
//        RestTemplate restTemplate = new RestTemplate(); // RESTful API 호출을 위한 RestTemplate 인스턴스를 생성합니다.
//        HttpHeaders headers = new HttpHeaders(); // HTTP 요청을 위한 헤더 객체를 생성합니다.
//        headers.setBearerAuth(accessToken); // Bearer 토큰을 사용하여 인증 헤더를 설정합니다.
//        HttpEntity<String> entity = new HttpEntity<>(headers); // HTTP 요청 본문 없이 헤더만 포함하는 HttpEntity 객체를 생성합니다.
//
//        // 카카오 로그아웃 API를 호출합니다. ResponseEntity 객체를 사용하여 응답을 받습니다.
//        ResponseEntity<String> response = restTemplate.exchange(
//                kakaoLogoutUrl, HttpMethod.POST, entity, String.class);
//
//        logger.info("카카오 로그아웃 완료");
//    }
//
//    public void invalidateRefreshToken(String refreshToken) {
//        // refreshToken 값을 기준으로 데이터베이스에서 리프레시 토큰 객체를 조회합니다.
//        User token = userRepository.findByRefreshToken(refreshToken)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token")); // 토큰이 없는 경우 예외를 발생시킵니다.
//
//        // 조회된 리프레시 토큰의 만료 상태를 true(만료됨)로 설정합니다.
//        token.setRefreshToken(null); // refreshToken 필드를 null로 설정하여 토큰을 무효화
//        userRepository.save(token); // 수정된 리프레시 토큰 객체를 데이터베이스에 저장합니다.
//    }
}
