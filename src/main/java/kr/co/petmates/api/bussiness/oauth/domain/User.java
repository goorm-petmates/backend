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

    private String accountEmail; // 카카오에서 카카오계정(accountEmail)으로 제공받은 이메일 저장
    private String nickname; // 카카오에서 닉네임(nickname)으로 제공받은 이름 저장
    private String profile_image; // 카카오에서 프로필 사진(profile_image)으로 제공받은 URL 저장

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
    public String getProfile_image() {
        return profile_image;
    }

    /**
     * 사용자 프로필 사진 URL을 설정합니다.
     *
     * @param profile_image 사용자 프로필 사진 URL
     */
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    /**
     * 사용자 이메일을 반환합니다.
     *
     * @return 사용자 이메일
     */
    public String getAccountEmail() {
        return accountEmail;
    }

    /**
     * 사용자 이메일을 설정합니다.
     *
     * @param accountEmail 사용자 이메일
     */
    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }
}