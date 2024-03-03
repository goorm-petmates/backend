package kr.co.petmates.api.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ERR_NOT_EXIST_PET ("등록된 반려동물이 없습니다.")
    ;
    private final String value;

    ErrorCode(String value) {
        this.value = value;
    }
}
