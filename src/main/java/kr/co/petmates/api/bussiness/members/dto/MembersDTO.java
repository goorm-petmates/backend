package kr.co.petmates.api.bussiness.members.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class MembersDTO {
    @JsonProperty("userNickName")
    private String nickname;

    @JsonProperty("userPhone")
    private String phone;

    @JsonProperty("userEmail")
    private String email;

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
