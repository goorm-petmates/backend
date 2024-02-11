// KakaoOauthController는 사용자의 인가 코드를 처리하고, JWT 토큰을 생성하여 반환하는 컨트롤러입니다.
package kr.co.petmates.api.bussiness.oauth.controller;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.bussiness.oauth.service.KakaoOauthService;
import kr.co.petmates.api.bussiness.oauth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired // UserService 객체를 자동으로 주입합니다.
    private UserService userService;

    @PostMapping("/login") // "/login" 경로로 POST 요청이 오면 이 메소드를 실행합니다.
    public ResponseEntity<?> kakaoLogin(HttpSession session, @RequestBody Map<String, String> requestBody) {
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
        UserService.AuthResult authResult = userService.createUserResult(userInfo);
        Map<String, Object> response = new HashMap<>();
        response.put("jwtToken", authResult.getJwtToken());
        response.put("refreshToken", authResult.getRefreshToken());
        response.put("isNewUser", authResult.isNewUser());

        // 여기서 responseBody를 ResponseEntity에 담아 반환하되, 변수명을 responseLogin으로 사용하고 싶은 의도를 반영
        ResponseEntity<Map<String, Object>> responseLogin = ResponseEntity.ok(response);

        return responseLogin;
    }
}