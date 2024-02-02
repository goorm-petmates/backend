// JWT (JSON Web Token) 토큰을 생성하고 검증

package kr.co.petmates.api.bussiness.oauth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component // 이 클래스를 스프링 빈으로 등록합니다.
public class JwtTokenProvider {

//    private String secretKey = "YOUR_SECRET_KEY"; // JWT 서명에 사용될 비밀 키입니다. 실제 서비스에서는 안전한 키를 사용해야 합니다.
    @Value("${yourSecretKey}")
    private String secretKey;

    // 사용자 식별자와 역할을 기반으로 JWT 토큰을 생성하는 메소드입니다.
    public String createToken(String userPk, String role) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT 클레임을 생성하고, 사용자 식별자를 주제로 설정합니다.
        claims.put("roles", role); // 사용자 역할 정보를 클레임에 추가합니다.

        Date now = new Date(); // 현재 시간을 생성합니다.
        Date validity = new Date(now.getTime() + + 24 * 60 * 60 * 1000); // 토큰의 유효 시간 24시간 설정합니다.

        // JWT 토큰을 생성하고 반환합니다.
        return Jwts.builder()
                .setClaims(claims) // 설정한 클레임을 JWT에 포함합니다.
                .setIssuedAt(now) // 토큰의 발행 시간을 설정합니다.
                .setExpiration(validity) // 토큰의 만료 시간을 설정합니다.
                .signWith(SignatureAlgorithm.HS256, secretKey) // HS256 알고리즘과 비밀 키를 사용하여 JWT를 서명합니다.
                .compact(); // JWT를 문자열로 압축하고 반환합니다.
    }

    // 전달받은 JWT 토큰이 유효한지 검증하는 메소드입니다.
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // 비밀 키를 사용하여 토큰을 파싱하고 검증합니다. 
            return true; // 토큰이 유효하면 true를 반환합니다.
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않거나 파싱 중 오류가 발생한 경우 처리합니다.
            return false; // 토큰이 유효하지 않으면 false를 반환합니다.
        }
    }
}