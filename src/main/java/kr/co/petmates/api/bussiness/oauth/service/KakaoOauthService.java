// getAccessToken:  인가 코드를 사용하여 카카오로부터 액세스 토큰을 받아오기
// getUserInfo: 엑세스 토큰으로 사용자 정보 가져오기

package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.oauth.client.KakaoApiClient;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoTokenResponse;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KakaoOauthService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
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

        logger.info("KakaoOauthService 저장된 엑세스토큰: {}", tokenStorage.getAccessToken());
        return accessToken;
    }

    // 엑세스 토큰으로 사용자 정보 가져오기
    public KakaoUserInfoResponse getUserInfo() {
        // TokenStorage에서 액세스 토큰 가져오기
        String accessToken = tokenStorage.getAccessToken();

        KakaoUserInfoResponse userInfo = kakaoApiClient.getUserInfo(accessToken);

        logger.info("KakaoOauthService.kakaoUserInfoResponse 사용자정보: {}", userInfo);
        return userInfo;
    }
}
