// Member 엔티티 클래스로, 사용자 정보를 데이터베이스에 저장하기 위한 모델 클래스입니다.

package kr.co.petmates.api.bussiness.oauth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;

@Entity
@Table(name = "members") // "member" 테이블과 매핑
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유한 멤버 ID

    private String nickname; // 카카오 프로필 닉네임
    private String kakaoAccount; // 카카오 계정 이메일
    private String profileImageUrl; // 프로필 사진 URL

    // 생성자
    public Member() {
    }

    // 생성자
    public Member(String profile_nickname, String account_email) {
        this.nickname = profile_nickname;
        this.kakaoAccount = account_email;
    }

    // 게터 및 세터 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getKakaoAccount() {
        return kakaoAccount;
    }

    public void setKakaoAccount(String kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", kakaoAccount='" + kakaoAccount + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
