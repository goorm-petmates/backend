// 사용자 정보를 데이터베이스에 저장하기 위한 User 엔티티 클래스입니다.
package kr.co.petmates.api.bussiness.oauth.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // 카카오에서 카카오계정(email)으로 제공받은 이메일 저장
    private String nickname; // 카카오에서 닉네임(nickname)으로 제공받은 이름 저장
    private String profileImage; // 카카오에서 프로필 사진(profileImage)으로 제공받은 URL 저장
    private String refreshToken;

//    private String roles; // 사용자의 역할을 저장하는 필드
//public String getRoles() {
//    return roles;
//}
//
//    public void setRoles(String roles) {
//        this.roles = roles;
//    }

    // 생성자
    public User() {
        // 기본 생성자
    }

    // Getter 및 Setter 메서드

    /**
     * 사용자 ID를 반환합니다.
     *
     * @return 사용자 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 사용자 ID를 설정합니다.
     *
     * @param id 사용자 ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 사용자 이름을 반환합니다.
     *
     * @return 사용자 이름
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 사용자 이름을 설정합니다.
     *
     * @param nickname 사용자 이름
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 사용자 프로필 사진 URL을 반환합니다.
     *
     * @return 사용자 프로필 사진 URL
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * 사용자 프로필 사진 URL을 설정합니다.
     *
     * @param profileImage 사용자 프로필 사진 URL
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * 사용자 이메일을 반환합니다.
     *
     * @return 사용자 이메일
     */
    public String getEmail() {
        return email;
    }

    /**
     * 사용자 이메일을 설정합니다.
     *
     * @param email 사용자 이메일
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 리프레시 토큰을 반환합니다.
     *
     * @return 리프레시 토큰
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * 리프레시 토큰을 설정합니다.
     *
     * @param refreshToken 리프레시 토큰
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}