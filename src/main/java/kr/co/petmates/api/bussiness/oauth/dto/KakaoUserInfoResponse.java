// 카카오 API로부터 반환된 사용자 정보 응답을 위한 DTO입니다.
package kr.co.petmates.api.bussiness.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KakaoUserInfoResponse {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    @Getter
    @JsonProperty("kakaoId")
    private Long kakaoId; // 사용자의 고유 ID

    @JsonProperty("connected_at")
    private String connectedAt; // 사용자가 카카오와 연결된 시간

    @Getter
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount; // 사용자의 카카오 계정 정보

    // 사용자의 카카오 계정 정보를 담는 내부 클래스입니다.
    public static class KakaoAccount {
        // Getters and Setters for KakaoAccount
        @Setter
        @Getter
        @JsonProperty("profile_nickname_needs_agreement")
        private boolean profileNicknameNeedsAgreement; // 닉네임 정보 제공 동의 여부

        @Setter
        @Getter
        @JsonProperty("profile_image_needs_agreement")
        private boolean profileImageNeedsAgreement; // 프로필 이미지 정보 제공 동의 여부

        // email 동의여부, 유효성 체크
        @Setter
        @Getter
        @JsonProperty("email_needs_agreement")
        private boolean emailNeedsAgreement; // 이메일 정보 제공 동의 여부

        @JsonProperty("is_email_valid")
        private boolean isEmailValid; // 이메일 유효 여부

        @JsonProperty("is_email_verified")
        private boolean isEmailVerified; // 이메일 검증 여부

        @Setter
        @Getter
        @JsonProperty("email")
        private String email; // 사용자 이메일

//        @Setter
        @Getter
        @JsonProperty("profile")
        private Profile profile; // 사용자의 프로필 정보입니다.

        @Setter
        @Getter
        private Long kakaoId;

        @Setter
        @Getter
        private String connectedAt;

        // 사용자의 프로필 정보를 담는 내부 클래스입니다.
        public static class Profile {
            // Getters and Setters
            // nickname
            @Setter
            @Getter
            @JsonProperty("nickname")
            private String nickname; // 사용자의 닉네임

            // thumbnailImageUrl
            @Setter
            @Getter
            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl; // 사용자의 썸네일 이미지 URL

            // profileImageUrl
            @Setter
            @Getter
            @JsonProperty("profile_image_url")
            private String profileImageUrl; // 사용자의 프로필 이미지 URL

            @Setter
            @Getter
            private boolean isEmailValid;

            @Setter
            @Getter
            private boolean isEmailVerified;
            @JsonProperty("is_default_image")
            private boolean isDefaultImage; // 기본 이미지 사용 여부

        }

        // 카카오 API 응답에서 'profile' JSON 객체를 profile 필드에 매핑
        @JsonProperty("profile")
        public void setProfile(Profile profile) {
            this.profile = profile;
        }
    }

    // Getters and Setters for KakaoUserInfoResponse

    // 카카오 API 응답에서 'kakao_account' JSON 객체를 kakaoAccount 필드에 매핑
    @JsonProperty("kakaoId")
    public void setKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }
    @JsonProperty("kakao_account")
    public void setKakaoAccount(KakaoAccount kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }

    // KakaoUserInfoResponse를 통해 직접 이메일을 가져오는 편의 메소드 추가
    public String getEmail() {
        if (kakaoAccount.getEmail() != null) {
            return kakaoAccount.getEmail();
        } else {
            return "기본 email"; // kakaoAccount가 null인 경우 빈 문자열 반환
        }
    }

    // 사용자 닉네임을 안전하게 가져오는 편의 메소드
    public String getNickname() {
//        if (kakaoAccount != null && kakaoAccount.getProfile() != null) {
        if (kakaoAccount.getProfile().getNickname() != null) {
            return kakaoAccount.getProfile().getNickname();
        } else {
            return "기본 닉네임"; // 닉네임을 가져올 수 없는 경우 기본 값을 제공
        }
    }

    // 사용자 프로필 이미지 URL을 안전하게 가져오는 편의 메소드
    public String getProfileImage() {
        if (kakaoAccount.getProfile().getProfileImageUrl() != null) {
            return kakaoAccount.getProfile().getProfileImageUrl();
        } else {
            return "기본 url"; // 프로필 이미지 URL을 가져올 수 없는 경우 빈 문자열을 제공
        }
    }

    // 객체를 JSON 형식의 문자열로 변환하는 toString 메소드입니다.
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);     // 객체를 JSON 문자열로 변환합니다.
        } catch (JsonProcessingException e) {
            return "KakaoUserInfoResponse{id=" + kakaoId + "}";      // JSON 변환 실패 시 기본 문자열을 반환합니다.
        }
    }
}





    // 방안1
//    private String nickname; // 닉네임
//    private String profileImage; // 프로필 사진
//    private String accountEmail; // 카카오 계정 이메일
//
//    // 닉네임(nickname) 필드의 getter 메서드
//    public String getNickname() {
//        return nickname;
//    }
//
//    // 닉네임(profileImage) 필드의 setter 메서드
//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }
//
//    // 프로필 사진(profileImage) 필드의 getter 메서드
//    public String getProfileImage() {
//        return profileImage;
//    }
//
//    // 프로필 사진(profileImage) 필드의 setter 메서드
//    public void setProfileImage(String profileImage) {
//        this.profileImage = profileImage;
//    }
//
//    // 카카오 계정 이메일(accountEmail) 필드의 getter 메서드
//    public String getAccountEmail() {
//        logger.info("계정 이메일(kakaoUserInfoResponse): {}", accountEmail);
//        return accountEmail;
//    }
//
//    // 카카오 계정 이메일(accountEmail) 필드의 setter 메서드
//    public void setAccountEmail(String accountEmail) {
//        this.accountEmail = accountEmail;
//    }
//}