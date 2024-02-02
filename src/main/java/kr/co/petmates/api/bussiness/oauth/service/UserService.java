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
        boolean isNewUser = !existingUser.isPresent(); // 기존 회원이 아니면 true, 기존 회원이면 false

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setName(userInfo.getProfile_nickname());
            user.setProfileImage(userInfo.getProfile_image());
            // 기존 회원 정보 업데이트, 추가 업데이트 필요한 정보가 있다면 여기에 추가
        } else {
            user = new User();
            user.setEmail(email);
            user.setName(userInfo.getProfile_nickname());
            user.setProfileImage(userInfo.getProfile_image());
            // 신규 회원 정보 저장, 추가 필요한 정보 설정
            isNewUser = true;
        }
        userRepository.save(user);

        return jwtTokenProvider.createToken(user.getEmail(), "ROLE_USER", isNewUser);
    }
}