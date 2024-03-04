package kr.co.petmates.api.enums;

public enum Role {
    ADMIN("관리자"),
    USER("일반회원"),
    PETSITTER("펫시터"),
    WITHDRAW("탈퇴회원");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
