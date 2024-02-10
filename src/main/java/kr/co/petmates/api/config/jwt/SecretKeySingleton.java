package kr.co.petmates.api.config.jwt;

import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecretKeySingleton {
    private static SecretKey secretKeyInstance = null;

    // 비공개 생성자를 사용하여 외부에서 인스턴스화를 방지합니다.
    private SecretKeySingleton() {
    }

    // SecretKey 인스턴스를 반환하는 메소드
    public static synchronized SecretKey getSecretKeyInstance() {
        if (secretKeyInstance == null) {
            try {
                // HS256 알고리즘을 사용하기 위한 SecretKey 생성
                KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
                secretKeyInstance = keyGen.generateKey();
            } catch (NoSuchAlgorithmException e) {
                // 알고리즘 오류 처리
                throw new RuntimeException("Failed to generate secret key", e);
            }
        }
        return secretKeyInstance;
    }
}
