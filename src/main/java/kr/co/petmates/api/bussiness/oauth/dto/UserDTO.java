// 저장된 사용자 정보 전송을 위한 DTO 클래스입니다. 저장된 사용자 데이터를 전송하는 데 필요한 필드를 포함합니다.

package kr.co.petmates.api.bussiness.oauth.dto;

public class UserDTO {
    private String accountEmail; // 카카오 계정 이메일
    private String profile_nickname; // 닉네임
    private String profile_image; // 프로필 사진 URL

    // 기본 생성자
    public UserDTO() {
    }

    // 생성자
    public UserDTO(String accountEmail, String profile_nickname, String profile_image) {
        this.accountEmail = accountEmail;
        this.profile_nickname = profile_nickname;
        this.profile_image = profile_image;
    }

    // Getters

    /**
     * 카카오 계정 이메일을 반환합니다.
     *
     * @return 카카오 계정 이메일
     */
    public String getAccountEmail() {
        return accountEmail;
    }

    /**
     * 사용자 닉네임을 반환합니다.
     *
     * @return 사용자 닉네임
     */
    public String getProfile_nickname() {
        return profile_nickname;
    }

    /**
     * 사용자 프로필 사진 URL을 반환합니다.
     *
     * @return 사용자 프로필 사진 URL
     */
    public String getProfile_image() {
        return profile_image;
    }

    // Setters

    /**
     * 카카오 계정 이메일을 설정합니다.
     *
     * @param accountEmail 카카오 계정 이메일
     */
    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    /**
     * 사용자 닉네임을 설정합니다.
     *
     * @param profile_nickname 사용자 닉네임
     */
    public void setProfile_nickname(String profile_nickname) {
        this.profile_nickname = profile_nickname;
    }

    /**
     * 사용자 프로필 사진 URL을 설정합니다.
     *
     * @param profile_image 사용자 프로필 사진 URL
     */
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
