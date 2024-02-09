// KakaoOauthController는 사용자의 인가 코드를 처리하고, JWT 토큰을 생성하여 반환하는 컨트롤러입니다.
package kr.co.petmates.api.bussiness.oauth.controller;

import java.util.Map;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.bussiness.oauth.service.KakaoOauthService;
import kr.co.petmates.api.bussiness.oauth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login") // "/login" 경로로 POST 요청이 오면 이 메소드를 실행합니다.
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        if (code == null) {
            logger.error("인가코드 비어있음");
            return ResponseEntity.badRequest().body("Authorization code is missing");
        }
        logger.info("인가코드: {}", code);
  // code = null 인 경우 체크,
    // 인가 코드를 사용하여 액세스 토큰을 요청합니다.
        String accessToken = kakaoOauthService.getAccessToken(code);
        logger.info("컨테이너 새로 생성된 엑세스토큰: {}", accessToken);

        // 액세스 토큰을 사용하여 사용자 정보를 요청하고 결과를 가져옵니다.
        KakaoUserInfoResponse userInfo = kakaoOauthService.getUserInfo();
        logger.info("컨테이너 사용자정보: {}", userInfo);

        // 사용자정보로 jwt 토큰, refresh 토큰 생성, 사용자정보 저장 요청
        UserService.AuthResult authResult = userService.createUserResult(userInfo);

        String email = userInfo.getEmail(); // 사용자 이메일을 추출하여 변수에 저장 //  -> 삭제
        logger.info("컨테이너 계정 이메일: {}", email);

        String jwtTokenTest = jwtTokenProvider.createJwtToken(email);   //    -> 삭제
        logger.info("컨테이너 jwt 토큰: {}", jwtTokenTest);

        String refreshTokenTest = jwtTokenProvider.createRefreshToken(email);   //    -> 삭제
        logger.info("컨테이너 refresh 토큰: {}", refreshTokenTest);

        // AuthResult 객체에서 jwtToken과 isNewUser 값을 추출
        String jwtToken = authResult.getJwtToken();
        String refreshToken = authResult.getRefreshToken();
        boolean isNewUser = authResult.isNewUser();

        // 로그로 jwtToken과 isNewUser 값을 출력
        logger.info("컨테이너 JWT Token: {}", jwtToken);
        logger.info("컨테이너 refreshToken: {}", refreshToken);
        logger.info("컨테이너 IsNewUser: {}", isNewUser);

        // 프론트엔드 URL에 JWT 토큰을 쿼리 파라미터로 추가하여 리다이렉트합니다.
        String redirectUrl = "http://localhost:3000/oauth/redirect/token?jwtToken=" + jwtToken + "&refreshToken=" + refreshToken + "&isNewUser=" + isNewUser;
        logger.info("컨테이너 redirectUrl: {}", redirectUrl);
        // HttpHeaders 객체를 생성합니다. 이 객체를 사용하여 HTTP 응답에 헤더를 추가할 수 있습니다.
        HttpHeaders headers = new HttpHeaders();
        // 'Location' 헤더에 리다이렉션할 URL을 추가합니다. 이 헤더는 클라이언트에게 새로운 위치로 이동하라는 지시를 담고 있습니다.
        headers.add("Location", redirectUrl);
        // ResponseEntity 객체를 생성하여 변수에 할당합니다. 이 객체는 설정한 헤더와 HTTP 상태 코드(여기서는 302 Found, 리다이렉션을 의미)를 함께 클라이언트에 전달합니다.
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(headers, HttpStatus.FOUND);
        logger.info("컨테이너 return: {}", responseEntity);
// 생성한 ResponseEntity 객체를 반환합니다.
        return responseEntity;
    }
}