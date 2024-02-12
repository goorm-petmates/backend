// jwt 토큰 갱신 요청

package kr.co.petmates.api.bussiness.oauth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.crypto.SecretKey;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.service.TokenCheckService;
import kr.co.petmates.api.config.jwt.SecretKeySingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<?> tokenUpdate(HttpServletResponse response, HttpServletRequest request) {
        // 요청 본문에서 "jwtToken"와 "refreshToken"을 추출하여 토큰 갱신 서비스를 호출합니다.
//        String jwtToken = requestBody.get("jwtToken");
//        String refreshToken = requestBody.get("refreshToken");
        String jwtToken = null;
        String refreshToken = null;
        logger.info("토큰갱신 초기 jwtToken: {}", jwtToken);

        // 요청으로부터 쿠키 배열을 가져옵니다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) { // "jwtToken" 쿠키를 찾습니다.
                    jwtToken = cookie.getValue();
                    logger.info("토큰갱신 쿠키에서 찾은 jwtToken: {}", jwtToken);
                } else if ("refreshToken".equals(cookie.getName())) { // "refreshToken" 쿠키를 찾습니다.
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (jwtToken == null || refreshToken == null) {
            return ResponseEntity.badRequest().body("Token not found in cookies"); // 쿠키에서 토큰을 찾지 못한 경우 오류 응답
        }


        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
        String email = claims.getSubject();
        logger.info("토큰갱신 찾은 email: {}", email);
        String newJwtToken = jwtTokenProvider.createJwtToken(email);
        logger.info("토큰갱신 갱신한 새로운 jwtToken: {}", newJwtToken);
        // 새로운 JWT 토큰을 쿠키에 저장
        Cookie newJwtTokenCookie = new Cookie("jwtToken", newJwtToken);
        newJwtTokenCookie.setHttpOnly(true);
        newJwtTokenCookie.setPath("/");
        newJwtTokenCookie.setMaxAge(-1);
        response.addCookie(newJwtTokenCookie);
        logger.info("토큰갱신 갱신 후 쿠키저장 newJwtTokenCookie: {}", newJwtTokenCookie);

        boolean isValidateRefreshToken = jwtTokenProvider.validateToken(refreshToken);
        logger.info("refreshToken 유효성: {}", isValidateRefreshToken);

        if (!isValidateRefreshToken) {
            String newRefreshToken = jwtTokenProvider.createRefreshToken(newJwtToken);

            // 새로운 리프레시 토큰을 쿠키에 저장
            Cookie newRefreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
            newRefreshTokenCookie.setHttpOnly(true);
            newRefreshTokenCookie.setPath("/");
            newRefreshTokenCookie.setMaxAge(-1);
            response.addCookie(newRefreshTokenCookie);

        }

// 응답 본문에 토큰 정보를 포함하지 않고, 쿠키를 통해 반환
        return ResponseEntity.ok().build();









//        Map<String, String> responseBody = new HashMap<>();
//
//        if(isValidateRefreshToken){
//            responseBody.put("jwtToken", newJwtToken);
//            responseBody.put("refreshToken", refreshToken);
//            logger.info("refreshToken 유효성 : true");
//        } else {
//            String newRefreshToken = jwtTokenProvider.createRefreshToken(newJwtToken);
//            responseBody.put("jwtToken", newJwtToken);
//            responseBody.put("refreshToken", newRefreshToken);
//            logger.info("refreshToken 유효성 : false");
//        }
//        logger.info("최종 토큰: {}", ResponseEntity.ok(responseBody));
//        // newJwtToken 변수를 반환합니다.
//        return ResponseEntity.ok(responseBody);
    }
}
