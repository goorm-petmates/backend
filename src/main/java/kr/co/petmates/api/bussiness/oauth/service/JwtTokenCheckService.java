package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenCheckService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 및 검증을 담당하는 프로바이더를 주입합니다.

    public String checkJwtToken(String jwtToken) {
        boolean isValidateToken = jwtTokenProvider.validateToken(jwtToken);   // jwtToken 유효성 체크
        if(isValidateToken) {
            String email = jwtTokenProvider.getEmail(jwtToken);;
            return email;
        } else {
            return "failed";
        }
    }
}
