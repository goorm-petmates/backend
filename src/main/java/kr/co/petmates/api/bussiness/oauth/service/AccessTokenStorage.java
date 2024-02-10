package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenStorage {
    private String accessToken; // 액세스 토큰을 저장할 변수


    // 사용자의 세션에 액세스 토큰 저장
    public void setAccessToken(HttpSession session, String accessToken) {
        session.setAttribute("accessToken", accessToken);
    }

    // 저장된 액세스 토큰 반환
    public String getAccessToken(HttpSession session) {
        return (String) session.getAttribute("accessToken");
    }
//    public String getAccessToken2() {
//        return accessToken;
//    }
//
//    public void setAccessToken2(String accessToken) {
//        this.accessToken = accessToken;
//    }
}
