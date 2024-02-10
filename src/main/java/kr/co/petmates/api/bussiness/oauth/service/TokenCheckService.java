package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenCheckService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 및 검증을 담당하는 프로바이더를 주입합니다.

    public String checkJwtToken(String jwtToken) {
        boolean isValidateToken = jwtTokenProvider.validateToken(jwtToken);   // jwtToken 유효성 체크
        if(isValidateToken) {
            String email = jwtTokenProvider.getEmail(jwtToken);;
            return email;
        } else {
            return "fail";
        }
        // 리프레시 토큰으로부터 사용자이메일, 엑세스토큰 추출합니다.
//        String email = jwtTokenProvider.getEmail(jwtToken);
//        String accessToken = jwtTokenProvider.getAccessToken(jwtToken);
//        jwtTokenProvider.createRefreshToken(jwtToken);

//        String newJwtToken = jwtTokenProvider.createJwtToken(email);
//        // 새로운 액세스 토큰을 생성하여 반환합니다.
//        return newJwtToken;
    }
}
