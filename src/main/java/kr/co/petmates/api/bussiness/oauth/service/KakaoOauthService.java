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
    private AccessTokenStorage accessTokenStorage; // AccessTokenStorage 추가

    // 인가 코드를 통해 카카오로부터 액세스 토큰을 받아오는 메소드
    public String getAccessToken(HttpSession session, String code) {
        KakaoTokenResponse kakaoTokenResponse = kakaoApiClient.getAccessToken(code);
        String accessToken = kakaoTokenResponse.getAccess_token();
        logger.info("KakaoOauthService 카카오api 통해 생선한 엑세스토큰, 반환값: {}", accessToken);

        // 액세스 토큰을 TokenStorage에 저장
//        accessTokenStorage.setAccessToken2(accessToken);
        accessTokenStorage.setAccessToken(session, accessToken);

        String storageAccessToken = accessTokenStorage.getAccessToken(session);
        logger.info("KakaoOauthService 생성후 저장된 엑세스토큰 accessTokenStorage.getAccessToken: {}", storageAccessToken);

        return accessToken;
    }

    // 엑세스 토큰으로 사용자 정보 가져오기
    public KakaoUserInfoResponse getUserInfo(HttpSession session) {
        // TokenStorage에서 액세스 토큰 가져오기
//        String accessToken = accessTokenStorage.getAccessToken2();
        String accessToken = accessTokenStorage.getAccessToken(session);
        logger.info("KakaoOauthService 저장된 엑세스토큰 가져오기: {}", accessToken);

        KakaoUserInfoResponse userInfo = kakaoApiClient.getUserInfo(accessToken);

        logger.info("KakaoOauthService.kakaoUserInfoResponse 사용자정보: {}", userInfo);
        return userInfo;
    }
}
