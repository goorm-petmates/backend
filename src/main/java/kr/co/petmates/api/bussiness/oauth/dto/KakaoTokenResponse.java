// 카카오 API로부터 반환된 토큰 응답을 위한 DTO입니다.

package kr.co.petmates.api.bussiness.oauth.dto;

public class KakaoTokenResponse {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;

    // getters and setters
    // access_token 필드의 getter 메서드
    public String getAccess_token() {
        return access_token;
    }

    // access_token 필드의 setter 메서드
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    // token_type 필드의 getter 메서드
    public String getToken_type() {
        return token_type;
    }

    // token_type 필드의 setter 메서드
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    // refresh_token 필드의 getter 메서드
    public String getRefresh_token() {
        return refresh_token;
    }

    // refresh_token 필드의 setter 메서드
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    // expires_in 필드의 getter 메서드
    public int getExpires_in() {
        return expires_in;
    }

    // expires_in 필드의 setter 메서드
    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    // scope 필드의 getter 메서드
    public String getScope() {
        return scope;
    }

    // scope 필드의 setter 메서드
    public void setScope(String scope) {
        this.scope = scope;
    }

    // refresh_token_expires_in 필드의 getter 메서드
    public int getRefresh_token_expires_in() {
        return refresh_token_expires_in;
    }

    // refresh_token_expires_in 필드의 setter 메서드
    public void setRefresh_token_expires_in(int refresh_token_expires_in) {
        this.refresh_token_expires_in = refresh_token_expires_in;
    }
}
