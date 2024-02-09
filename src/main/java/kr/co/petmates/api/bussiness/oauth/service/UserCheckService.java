package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.oauth.domain.User;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.bussiness.oauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCheckService {
    @Autowired
    private UserRepository userRepository;

    // isNewUser 값 반환, 이메일로 데이터베이스 조회
    public boolean isNewUser(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }

    // 사용자 존재 여부를 확인하고 업데이트하거나 새로 저장합니다.
    public void saveOrUpdateUser(KakaoUserInfoResponse userInfo, String refreshToken) {
        String email = userInfo.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseGet(User::new); // 기존 사용자가 없을 경우 새 User 객체 생성

        // 사용자 정보를 업데이트하거나 설정합니다.
        user.setEmail(email);
        user.setKakaoId((userInfo.getKakaoAccount().getKakaoId()));
        user.setNickname(userInfo.getNickname());
        user.setProfileImage(userInfo.getProfileImage());
        user.setRefreshToken(refreshToken);

        userRepository.save(user); // 변경된 정보를 저장하거나 신규 사용자를 저장합니다.
    }
}
