// JWT (JSON Web Token) 토큰을 생성하고 검증

package kr.co.petmates.api.bussiness.oauth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.crypto.SecretKey;
import kr.co.petmates.api.config.jwt.SecretKeySingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    SecretKey secretKey = SecretKeySingleton.getSecretKeyInstance();

    public String createJwtToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 6 * 60 * 60 * 1000); // 6 hours

        // 이메일 정보 포함
        String jwtToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

//        logger.info("JWT 토큰 생성(jwtTokenProvider): token={}", jwtToken);

        return jwtToken;
    }

    public String createRefreshToken(String jwtToken) {
        // 기존 토큰에서 이메일 추출
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
        String email = claims.getSubject();

//        logger.info("(jwtTokenProvider): 사용자 이메일 추출={}", email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + 30L * 24 * 60 * 60 * 1000); // 30 days

        // 이메일 정보 포함
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

//        logger.info("Refresh 토큰 생성(jwtTokenProvider): token={}", refreshToken);

        return refreshToken;
    }

    // 전달받은 JWT 토큰이 유효한지 검증하는 메소드입니다.
    public boolean validateToken(String jwtToken) {
        try {
            logger.info("JWT 토큰 유효성 체크함수 전달받은 토큰: jwtToken={}", jwtToken);
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken);
            return true; // 토큰이 유효하면 true를 반환합니다.
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT 토큰 유효하지 않음: token={}, error={}", jwtToken, e.getMessage());
            // 토큰이 유효하지 않거나 파싱 중 오류가 발생한 경우 처리합니다.
            return false; // 토큰이 유효하지 않으면 false를 반환합니다.
        }
    }

    // JWT에서 사용자 이메일 추출
    public String getEmail(String jwtToken) throws JwtException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
        String getEmail = claims.getSubject();

        return getEmail; // 사용자 이메일 추출
    }
}