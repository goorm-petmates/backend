// jwt 토큰 갱신 요청

package kr.co.petmates.api.bussiness.oauth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.service.TokenCheckService;
import kr.co.petmates.api.config.jwt.SecretKeySingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth/jwt") // 이 컨트롤러의 기본 경로를 "/api/oauth/jwt"로 설정합니다.
public class TokenUpdateController {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    SecretKey secretKey = SecretKeySingleton.getSecretKeyInstance();
//    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 안전한 키 생성
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private TokenCheckService tokenCheckService; // 토큰 갱신 로직을 담당하는 서비스를 주입합니다.

    @PostMapping("/update") // "/api/oauth/jwt/update" 경로로 POST 요청을 받는 메소드를 정의합니다.
    public ResponseEntity<?> tokenUpdate(@RequestBody Map<String, String> request) {
        // 요청 본문에서 "jwtToken"와 "refreshToken"을 추출하여 토큰 갱신 서비스를 호출합니다.
        String jwtToken = request.get("jwtToken");
        String refreshToken = request.get("refreshToken");

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
        String email = claims.getSubject();
        String newJwtToken = jwtTokenProvider.createJwtToken(email);

        boolean isValidateRefreshToken = jwtTokenProvider.validateToken(refreshToken);
        logger.info("refreshToken 유효성: {}", isValidateRefreshToken);

        Map<String, String> response = new HashMap<>();

        if(isValidateRefreshToken){
            response.put("jwtToken", newJwtToken);
            response.put("refreshToken", refreshToken);
            logger.info("refreshToken 유효성 : true");
        } else {
            String newRefreshToken = jwtTokenProvider.createRefreshToken(newJwtToken);
            response.put("jwtToken", newJwtToken);
            response.put("refreshToken", newRefreshToken);
            logger.info("refreshToken 유효성 : false");
        }

        ResponseEntity<?> newToken = ResponseEntity.ok(response);
        logger.info("최종 토큰: {}", newToken);
        // newJwtToken 변수를 반환합니다.
        return newToken;
    }
}
