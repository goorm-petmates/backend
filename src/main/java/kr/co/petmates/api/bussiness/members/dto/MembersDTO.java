package kr.co.petmates.api.bussiness.members.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import kr.co.petmates.api.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MembersDTO {
    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    @Email
    private String email;

    @JsonProperty("profileImage")
    private String profileImage;

    private Long kakaoId;

    private Boolean isWithdrawn; // 탈퇴여부

    private Role role;

    @JsonProperty("fullAddr")
    private String fullAddr;

    @JsonProperty("roadAddr")
    private String roadAddr;

    @JsonProperty("detailAddr")
    private String detailAddr;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("zipcode")
    private String zipcode;
}
