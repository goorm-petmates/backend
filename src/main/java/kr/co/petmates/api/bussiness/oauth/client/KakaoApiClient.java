package kr.co.petmates.api.bussiness.oauth.client;

import jakarta.servlet.http.HttpSession;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoLogoutResponse;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoTokenResponse;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.bussiness.oauth.service.AccessTokenStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoApiClient {

    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.token-endpoint}")
    private String kakaoTokenEndpoint;

    @Value("${kakao.userinfo-endpoint}")
    private String kakaoUserInfoEndpoint;

    @Value("${kakao.logout-endpoint}")
    private String logoutEndpoint;

    private final RestTemplate restTemplate;
    @Autowired
    private AccessTokenStorage accessTokenStorage;

    public KakaoApiClient() {
        this.restTemplate = new RestTemplate();
    }

    // 인가코드로 엑세스토큰 요청하기
    public KakaoTokenResponse getAccessToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                kakaoTokenEndpoint,
                HttpMethod.POST,
                request,
                KakaoTokenResponse.class
        );

        logger.info("kakaoApiClient 카카오 요청해서 생성한 엑세스토큰: {}", response.getBody());
        return response.getBody();
    }

    // 엑세스토큰으로 사용자정보 요청하기
    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // HttpEntity에 헤더만 설정 (body는 null)
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // GET 요청 보내기
        ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
                kakaoUserInfoEndpoint,
                HttpMethod.GET,
                requestEntity,
                KakaoUserInfoResponse.class
        );

        KakaoUserInfoResponse userInfo = response.getBody();

        logger.info("kakaoApiClient 사용자정보: {}", userInfo);
        return userInfo;
    }

    // 카카오로그아웃 요청
    public boolean kakaoLogout(HttpSession session) {
        String accessToken = accessTokenStorage.getAccessToken(session);
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        // HttpEntity에 헤더 설정 (body는 필요 없으므로 null)
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoLogoutResponse> response = restTemplate.exchange(
                logoutEndpoint,
                HttpMethod.POST,
                entity,
                KakaoLogoutResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {   // 상태 코드가 200 OK인 경우, 요청이 성공적으로 처리되었다고 간주합니다.
            logger.info("로그아웃 성공 true 반환");
            return true;
        } else {    // 그 외의 경우, 요청 처리가 실패했다고 간주합니다.
            logger.info("로그아웃 실패 false 반환");
            return false;
        }
    }

    public void updateAccessToken(HttpSession session, String refreshToken) {
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 본문(body) 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("refresh_token", refreshToken);

        // HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // POST 요청 실행
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                kakaoTokenEndpoint,
                HttpMethod.POST,
                requestEntity,
                KakaoTokenResponse.class
        );

        String newAccessToken = response.getBody().getAccess_token();
        String newRefreshToken = response.getBody().getRefresh_token();
        logger.info("로그아웃 갱신 후 엑세스토큰: {}", newAccessToken);
        logger.info("로그아웃 갱신 후 리프레시토큰: {}", newRefreshToken);
        accessTokenStorage.setAccessToken(session, newAccessToken, newRefreshToken);
//        return newAccessToken, newRefreshToken;
    }
}
