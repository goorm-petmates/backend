// 카카오 API로부터 반환된 사용자 정보 응답을 위한 DTO입니다.
package kr.co.petmates.api.bussiness.oauth.dto;

import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KakaoUserInfoResponse {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    private String nickname; // 닉네임
    private String profile_image; // 프로필 사진
    private String accountEmail; // 카카오 계정 이메일

    // 닉네임(nickname) 필드의 getter 메서드
    public String getNickname() {
        return nickname;
    }

    // 닉네임(profile_nickname) 필드의 setter 메서드
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // 프로필 사진(profile_image) 필드의 getter 메서드
    public String getProfile_image() {
        return profile_image;
    }

    // 프로필 사진(profile_image) 필드의 setter 메서드
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    // 카카오 계정 이메일(accountEmail) 필드의 getter 메서드
    public String getAccountEmail() {
        logger.info("계정 이메일(kakaoUserInfoResponse): {}", accountEmail);
        return accountEmail;
    }

    // 카카오 계정 이메일(accountEmail) 필드의 setter 메서드
    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }
}