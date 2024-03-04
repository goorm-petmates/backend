// getAccessToken:  인가 코드를 사용하여 카카오로부터 액세스 토큰을 받아오기
// getUserInfo: 엑세스 토큰으로 사용자 정보 가져오기

package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.servlet.http.HttpSession;
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
    private AccessTokenStorage accessTokenStorage;

    // 인가 코드를 통해 카카오서버로부터 액세스토큰 받아오기 -> 세션 저장
    public void getAccessToken(HttpSession session, String code) {
        KakaoTokenResponse kakaoTokenResponse = kakaoApiClient.getAccessToken(code);

        String accessToken = kakaoTokenResponse.getAccess_token();
        String refreshToken = kakaoTokenResponse.getRefresh_token();
        logger.info("KakaoOauthService 카카오api 통해 생성한 엑세스토큰, 반환값: {}", accessToken);
//        logger.info("KakaoOauthService 카카오api 통해 생성한 refreshToken, 반환값: {}", refreshToken);

        // 액세스 토큰 세션 저장
        accessTokenStorage.setAccessToken(session, accessToken, refreshToken);
    }

    // 엑세스 토큰으로 카카오서버로부터 사용자 정보 가져오기
    public KakaoUserInfoResponse getUserInfo(HttpSession session) {
        String accessToken = accessTokenStorage.getAccessToken(session);
//        logger.info("KakaoOauthService 세션에 저장된 엑세스토큰값: {}", accessToken);
        KakaoUserInfoResponse userInfo = kakaoApiClient.getUserInfo(accessToken);
        return userInfo;
    }
}
