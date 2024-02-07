package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.domain.User;
import kr.co.petmates.api.bussiness.oauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenRefreshService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 및 검증을 담당하는 프로바이더를 주입합니다.

    @Autowired
    private UserRepository userRepository; // 사용자 정보를 조회 및 관리하는 레포지토리를 주입합니다.

    public String refreshToken(String refreshToken) {
        // 리프레시 토큰을 받아 새 액세스 토큰을 생성하는 메소드입니다.
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            // 리프레시 토큰의 유효성을 검증합니다. 유효하지 않으면 예외를 던집니다.
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // 리프레시 토큰으로부터 사용자의 이메일(또는 사용자 식별자)을 추출합니다.
        String accountEmail = jwtTokenProvider.getUserPk(refreshToken);

        // 추출한 이메일을 사용하여 데이터베이스에서 사용자를 조회합니다.
        User user = userRepository.findByAccountEmail(accountEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found")); // 사용자를 찾지 못하면 예외를 던집니다.

        // 새로운 액세스 토큰을 생성하여 반환합니다.
        return jwtTokenProvider.createJwtToken(accountEmail);
    }
}
