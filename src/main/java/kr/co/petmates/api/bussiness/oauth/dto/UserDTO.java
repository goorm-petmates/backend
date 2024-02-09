// 저장된 사용자 정보 전송을 위한 DTO 클래스입니다. 저장된 사용자 데이터를 전송하는 데 필요한 필드를 포함합니다.

package kr.co.petmates.api.bussiness.oauth.dto;

public class UserDTO {
    private String email; // 카카오 계정 이메일
    private String nickname; // 닉네임
    private String profileImage; // 프로필 사진 URL

    // 기본 생성자
    public UserDTO() {
    }

    // 생성자
    public UserDTO(String email, String nickname, String profileImage) {
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    // Getters

    /**
     * 카카오 계정 이메일을 반환합니다.
     *
     * @return 카카오 계정 이메일
     */
    public String getEmail() {
        return email;
    }

    /**
     * 사용자 닉네임을 반환합니다.
     *
     * @return 사용자 닉네임
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 사용자 프로필 사진 URL을 반환합니다.
     *
     * @return 사용자 프로필 사진 URL
     */
    public String getProfileImage() {
        return profileImage;
    }

    // Setters

    /**
     * 카카오 계정 이메일을 설정합니다.
     *
     * @param email 카카오 계정 이메일
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 사용자 닉네임을 설정합니다.
     *
     * @param nickname 사용자 닉네임
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 사용자 프로필 사진 URL을 설정합니다.
     *
     * @param profileImage 사용자 프로필 사진 URL
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
