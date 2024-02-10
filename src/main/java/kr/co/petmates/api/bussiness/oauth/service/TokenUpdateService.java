package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenUpdateService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 및 검증을 담당하는 프로바이더를 주입합니다.

    public String updateJwtToken(String jwtToekn) {
        // 리프레시 토큰으로부터 사용자이메일, 엑세스토큰 추출합니다.
        String email = jwtTokenProvider.getEmail(jwtToekn);
//        String accessToken = jwtTokenProvider.getAccessToken(jwtToekn);
//        jwtTokenProvider.createRefreshToken(jwtToekn);

        String newJwtToken = jwtTokenProvider.createJwtToken(email);
        // 새로운 액세스 토큰을 생성하여 반환합니다.
        return newJwtToken;
    }
}
