package kr.co.petmates.api.bussiness.members.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.io.Serializable;
import java.time.LocalDateTime;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.common.entity.BaseDateTimeEntity;
import kr.co.petmates.api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Builder
@NoArgsConstructor
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
//
//    @Id
//    private Long kakaoId;

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
    private Boolean isWithdrawn; // 탈퇴여부

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "members", fetch = FetchType.LAZY)
    private Petsitter petsitter;

//    @ManyToMany
//    @JoinTable(
//            name = "USER_AUTHORITY",
//            joinColumns = {@JoinColumn(name = "usersId", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "authorityName", referencedColumnName = "authorityName")})
//    private Set<Authority> authorities;
}
