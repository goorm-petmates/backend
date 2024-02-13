package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenStorage {
    // 사용자의 세션에 액세스 토큰 저장
    public void setAccessToken(HttpSession session, String accessToken, String refreshToken) {
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("refreshToken", refreshToken);
    }

    // 저장된 액세스 토큰 반환
    public String getAccessToken(HttpSession session) {
        return (String) session.getAttribute("accessToken");
    }

    // 저장된 리프레스 토큰 반환
    public String getRefreshToken(HttpSession session) {
        return (String) session.getAttribute("refreshToken");
    }
}
