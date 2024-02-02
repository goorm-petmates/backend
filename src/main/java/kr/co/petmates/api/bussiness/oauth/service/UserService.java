// UserService는 사용자 정보를 처리하고 JWT 토큰을 생성하는 서비스입니다.
package kr.co.petmates.api.bussiness.oauth.service;

import java.util.Optional;
import kr.co.petmates.api.bussiness.oauth.client.KakaoApiClient;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.domain.User;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.bussiness.oauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private KakaoApiClient kakaoApiClient;

    @Autowired
    private UserRepository userRepository;

    // 카카오 액세스 토큰을 사용하여 JWT 토큰을 생성하는 메소드
    public String createJwtToken(String accessToken) {
        // KakaoApiClient를 통해 카카오 사용자 정보를 가져옵니다.
        KakaoUserInfoResponse userInfo = kakaoApiClient.getUserInfo(accessToken);

        // 카카오 사용자 정보에서 필요한 정보 추출
        String nickname = userInfo.getProfile_nickname();
        String profileImage = userInfo.getProfile_image();
        String email = userInfo.getAccount_email();

        // 사용자 정보를 데이터베이스에서 찾습니다.
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            // 이미 데이터베이스에 사용자가 존재한다면, 사용자 정보 업데이트
            User user = existingUser.get();
            user.setName(nickname);
            user.setProfileImage(profileImage);
            // 기타 필요한 정보 업데이트

            userRepository.save(user);

            // JWT 토큰을 생성하고 반환합니다.
            return jwtTokenProvider.createToken(email, "ROLE_USER");
        } else {
            // 데이터베이스에 사용자가 존재하지 않는 경우, 새로운 사용자 정보를 생성하고 저장합니다.
            User user = new User();
            user.setName(nickname);
            user.setProfileImage(profileImage);
            user.setEmail(email);
            // 기타 필요한 정보 설정
            userRepository.save(user);

            // JWT 토큰을 생성하고 반환합니다.
            return jwtTokenProvider.createToken(email, "ROLE_USER");
        }
    }
}