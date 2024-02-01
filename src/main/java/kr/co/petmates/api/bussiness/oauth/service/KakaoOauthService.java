// KakaoOauthService는 인가 코드를 사용하여 카카오로부터 액세스 토큰을 받아오는 역할을 합니다.

package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.oauth.client.KakaoApiClient;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoTokenResponse;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KakaoOauthService {

    @Autowired
    private KakaoApiClient kakaoApiClient;

    @Autowired
    private TokenStorage tokenStorage; // TokenStorage 추가

    // 인가 코드를 통해 카카오로부터 액세스 토큰을 받아오는 메소드
    public String getAccessToken(String authorizationCode) {
        KakaoTokenResponse tokenResponse = kakaoApiClient.getAccessToken(authorizationCode);
        String accessToken = tokenResponse.getAccess_token();

        // 액세스 토큰을 TokenStorage에 저장
        tokenStorage.setAccessToken(accessToken);

        return accessToken;
    }

    public KakaoUserInfoResponse getUserInfo() {
        // TokenStorage에서 액세스 토큰 가져오기
        String accessToken = tokenStorage.getAccessToken();

        KakaoUserInfoResponse userInfo = kakaoApiClient.getUserInfo(accessToken);
        return userInfo;
    }
}
