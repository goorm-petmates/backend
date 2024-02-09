package kr.co.petmates.api.bussiness.members.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import kr.co.petmates.api.common.entity.BaseDateTimeEntity;
import kr.co.petmates.api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Builder
//@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@JsonIgnoreProperties({ "pwd" })
@Getter
@ToString
public class Members extends BaseDateTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id; // 시퀀스

    @Id
    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false, length = 255)
    private String nickname; // 닉네임

    private String profileImage; // 카카오에서 프로필 사진(profileImage)으로 제공받은 URL 저장
    private String refreshToken;

    @Column(length = 16)
    private String phone; // 휴대폰 번호

    @Column(length = 8)
    private String zipcode; // 우편번호

    @Column(nullable = false, length = 512)
    private String roadAddr; // 도로명 주소

    @Column(length = 512)
    private String detailAddr; // 나머지 주소

    @Column(length = 20)
    private String latitude; // 위도

    @Column(length = 20)
    private String longitude; // 경도

    @Column(name = "last_login_at", nullable = true, columnDefinition = "TIMESTAMP")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime lastLoginDate; // 마지막 로그인 일시

    @Column
    private boolean isWithdrawn; // 탈퇴여부

    @Enumerated(EnumType.STRING)
    private Role role;



    // 생성자
    public Members() {
        // 기본 생성자
    }

    // Getter 및 Setter 메서드

    /**
     * 사용자 ID를 반환합니다.
     *
     * @return 사용자 ID
     */
    public Long getKakaoId() {
        return kakaoId;
    }

    /**
     * 사용자 ID를 설정합니다.
     *
     * @param kakaoId 사용자 ID
     */
    public void setKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
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

//    @ManyToMany
//    @JoinTable(
//            name = "USER_AUTHORITY",
//            joinColumns = {@JoinColumn(name = "usersId", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "authorityName", referencedColumnName = "authorityName")})
//    private Set<Authority> authorities;
}
