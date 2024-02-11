package kr.co.petmates.api.bussiness.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoLogoutResponse {
    @JsonProperty("id") // JSON의 'id' 필드를 kakaoId 필드에 매핑
    private Long kakaoId;

    // 기본 생성자
    public KakaoLogoutResponse() {
    }

    // 모든 필드를 포함한 생성자
    public KakaoLogoutResponse(Long kakaoId) {
        this.kakaoId = kakaoId;
    }

    // getter
    public Long getKakaoId() {
        return kakaoId;
    }

    // setter
    public void setKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }

    // toString 메서드
    @Override
    public String toString() {
        return "KakaoLogoutResponse{" +
                "kakaoId=" + kakaoId +
                '}';
    }
}
