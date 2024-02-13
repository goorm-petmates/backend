package kr.co.petmates.api.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;

public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 900000)) // 토큰 유효 기간
//                .signWith(SignatureAlgorithm.HS512, secretKey)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
