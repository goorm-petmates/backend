package kr.co.petmates.api.bussiness.oauth.service;
import org.springframework.stereotype.Component;

@Component
public class TokenStorage {
    private String accessToken; // 액세스 토큰을 저장할 변수

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
