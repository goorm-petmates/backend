package kr.co.petmates.api.bussiness.members.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import kr.co.petmates.api.bussiness.members.dto.MembersDTO;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.common.entity.BaseDateTimeEntity;
import kr.co.petmates.api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "MEMBERS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
//@JsonIgnoreProperties({ "pwd" })
@Getter
@Setter
@ToString
public class Members extends BaseDateTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id; // 시퀀스

    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String email; // 카카오에서 카카오계정(email)으로 제공받은 이메일 저장

    @Column(length = 255)
    private String nickname; // 카카오에서 닉네임(nickname)으로 제공받은 이름 저장

    private String profileImage;

    @Column(length = 16)
    private String phone; // 휴대폰 번호

    @Column(length = 8)
    private String zipcode; // 우편번호

    @Column(length = 1024)
    private String fullAddr; // 전체 주소

    @Column(length = 512)
    private String roadAddr; // 도로명 주소

    @Column(length = 512)
    private String detailAddr; // 나머지 주소

    @Column(length = 512)
    private Double latitude; // 위도

    @Column(length = 512)
    private Double longitude; // 경도

    @Column(name = "last_login_at", nullable = true, columnDefinition = "TIMESTAMP")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime lastLoginDate; // 마지막 로그인 일시

    @Column
    private Boolean isWithdrawn; // 탈퇴여부

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Members toMembersEntity(MembersDTO membersDTO) { // dto -> entity
        Members member = new Members();
        member.setKakaoId(membersDTO.getKakaoId());
        member.setEmail(membersDTO.getEmail());
        member.setNickname(membersDTO.getNickname());
        member.setProfileImage(membersDTO.getProfileImage());
        member.setPhone(membersDTO.getPhone());
        member.setZipcode(membersDTO.getZipcode());
        member.setFullAddr(membersDTO.getFullAddr());
        member.setRoadAddr(membersDTO.getRoadAddr());
        member.setDetailAddr(membersDTO.getDetailAddr());
        member.setRole(membersDTO.getRole());
        member.setLongitude(membersDTO.getLongitude());
        member.setLatitude(membersDTO.getLatitude());
        member.setIsWithdrawn(membersDTO.getIsWithdrawn());
        return member;
    }

    @OneToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private List<Petsitter> petsitter;      // 펫시터

//    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
//    private List<Pet> pets;


//    @OneToOne(mappedBy = "members", fetch = FetchType.LAZY)
//    private Petsitter petsitter;
//
//    @ManyToMany
//    @JoinTable(
//            name = "USER_AUTHORITY",
//            joinColumns = {@JoinColumn(name = "usersId", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "authorityName", referencedColumnName = "authorityName")})
//    private Set<Authority> authorities;
}
