// jwt 토큰 갱신 요청

package kr.co.petmates.api.bussiness.oauth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.service.JwtTokenSaveService;
import kr.co.petmates.api.config.jwt.SecretKeySingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth/jwt")
public class TokenUpdateController {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    SecretKey secretKey = SecretKeySingleton.getSecretKeyInstance();
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtTokenSaveService jwtTokenSaveService;

    @PostMapping("/update")   // Jwt 토큰 갱신 요청
    public ResponseEntity<?> tokenUpdate(HttpServletResponse response, HttpServletRequest request) {
        String jwtToken = null;
        String refreshToken = null;
        logger.info("토큰갱신 초기 변수 jwtToken: {}", jwtToken);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    logger.info("토큰갱신 쿠키에서 찾은 jwtToken: {}", jwtToken);
                } else if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (jwtToken == null || refreshToken == null) {
            return ResponseEntity.badRequest().body("쿠키에서 토큰을 찾지 못함");
        }

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();

        String email = claims.getSubject();
        logger.info("토큰갱신 찾은 email: {}", email);
        String newJwtToken = jwtTokenProvider.createJwtToken(email);
        logger.info("토큰갱신 갱신한 새로운 jwtToken: {}", newJwtToken);

        jwtTokenSaveService.saveTokenToCookies(newJwtToken, refreshToken, response);

        boolean isValidateRefreshToken = jwtTokenProvider.validateToken(refreshToken);
        logger.info("refreshToken 유효성: {}", isValidateRefreshToken);

        if (!isValidateRefreshToken) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("result", "failed");
            responseBody.put("data", "재로그인 해주세요!");
            return ResponseEntity.ok().body(responseBody);
        }

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("result", "success");
        return ResponseEntity.ok(responseBody);
    }
}
