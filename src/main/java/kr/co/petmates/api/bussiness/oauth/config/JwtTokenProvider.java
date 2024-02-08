// JWT (JSON Web Token) 토큰을 생성하고 검증

package kr.co.petmates.api.bussiness.oauth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import kr.co.petmates.api.bussiness.oauth.repository.UserRepository;
import kr.co.petmates.api.bussiness.oauth.service.KakaoOauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // 이 클래스를 스프링 빈으로 등록합니다.
public class JwtTokenProvider {

    @Autowired // 스프링의 의존성 주입 기능을 사용하여 KakaoOauthService 객체를 자동으로 주입합니다.
    private KakaoOauthService kakaoOauthService;

    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    //    private String secretKey = "YOUR_SECRET_KEY"; // JWT 서명에 사용될 비밀 키입니다. 실제 서비스에서는 안전한 키를 사용해야 합니다.
//    @Value("${jwt.secret}")
//    private String secretKey;
    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 안전한 키 생성

    public String createJwtToken(String email) {
        logger.info("(jwtTokenProvider): 사용자 이메일={}", email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + 6 * 60 * 60 * 1000); // 6 hours

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        logger.info("JWT 토큰 생성(jwtTokenProvider): token={}", token);

        return token;
    }
//

    // 전달받은 JWT 토큰이 유효한지 검증하는 메소드입니다.
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // 비밀 키를 사용하여 토큰을 파싱하고 검증합니다. 
            logger.info("JWT 토큰 유효성 체크: token={}", token);
            return true; // 토큰이 유효하면 true를 반환합니다.
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT 토큰 유효하지 않음: token={}, error={}", token, e.getMessage());
            // 토큰이 유효하지 않거나 파싱 중 오류가 발생한 경우 처리합니다.
            return false; // 토큰이 유효하지 않으면 false를 반환합니다.
        }
    }

    public String getUserPk(String token) {
        // 토큰에서 클레임을 추출합니다.
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        // 'subject' 클레임에서 사용자 식별자를 반환합니다.
        return claims.getSubject();
    }
}

//
//
//    // isNewUser 정보를 포함하는 JWT 토큰을 생성합니다.
//    public String createToken(String userPk, boolean isNewUser) {
//        Claims claims = Jwts.claims().setSubject(userPk);
//        claims.put("isNewUser", isNewUser); // 추가 정보 포함
//
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + 6 * 60 * 60 * 1000); // 6 hours
//
//        String token = Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//
//        logger.info("JWT 토큰 생성: userPk={}, isNewUser={}, token={}", userPk, isNewUser, token);
//
//        return token;
//    }