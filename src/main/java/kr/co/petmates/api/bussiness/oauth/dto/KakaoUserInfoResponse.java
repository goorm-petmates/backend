// 카카오 API로부터 반환된 사용자 정보 응답을 위한 DTO입니다.
package kr.co.petmates.api.bussiness.oauth.dto;

public class KakaoUserInfoResponse {
    private String profile_nickname; // 닉네임
    private String profile_image; // 프로필 사진
    private String account_email; // 카카오 계정 이메일

    // 닉네임(profile_nickname) 필드의 getter 메서드
    public String getProfile_nickname() {
        return profile_nickname;
    }

    // 닉네임(profile_nickname) 필드의 setter 메서드
    public void setProfile_nickname(String profile_nickname) {
        this.profile_nickname = profile_nickname;
    }

    // 프로필 사진(profile_image) 필드의 getter 메서드
    public String getProfile_image() {
        return profile_image;
    }

    // 프로필 사진(profile_image) 필드의 setter 메서드
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    // 카카오 계정 이메일(account_email) 필드의 getter 메서드
    public String getAccount_email() {
        return account_email;
    }

    // 카카오 계정 이메일(account_email) 필드의 setter 메서드
    public void setAccount_email(String account_email) {
        this.account_email = account_email;
    }
}