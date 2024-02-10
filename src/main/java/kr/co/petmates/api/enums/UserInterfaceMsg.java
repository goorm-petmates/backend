package kr.co.petmates.api.enums;

import lombok.Getter;

@Getter
public enum UserInterfaceMsg {
    VALIDATE_PHONE_NUMBER("전화번호 형식을 확인해주세요"),
    ERR_SAVE_INPUT_TYPE ("올바르지 않은 형식입니다."),
    ERR_NOT_REQUIRED_FIELD ("필수값이 모두 입력되지 않았습니다"),
    ERR_UPLOAD_FAILED ("업로드 실패"),
    ERR_UPLOAD_OVER_SIZE ("업로드 허용 용량 초과"),
    ERR_UPLOAD_NOT_VALID_EXT ("허용된 파일이 아닙니다."),
    ERR_NOT_EXIST_PET ("등록된 반려동물이 없습니다."),
    ERR_NOT_EXIST_RESERVED("예약된 내역이 없습니다.")
    ;
    private final String value;

    UserInterfaceMsg(String value) {
        this.value = value;
    }

}
