package kr.co.petmates.api.bussiness.members.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class MembersDTO {
    @Getter
    @Setter
    @JsonProperty("userNickName")
    private String nickname;

    @Getter
    @Setter
    @JsonProperty("userPhone")
    private String phone;

    @Getter
    @Setter
    @JsonProperty("userEmail")
    private String email;

    @Getter
    @Setter
    @JsonProperty("fullAddr")
    private String fullAddr;

    @Getter
    @Setter
    @JsonProperty("roadAddr")
    private String roadAddr;

    @Getter
    @Setter
    @JsonProperty("detailAddr")
    private String detailAddr;

    @Getter
    @Setter
    @JsonProperty("latitude")
    private Double latitude;

    @Getter
    @Setter
    @JsonProperty("longitude")
    private Double longitude;

    @Getter
    @Setter
    @JsonProperty("zipcode")
    private String zipcode;
}
