// KakaoApiClient는 카카오 API와의 직접적인 통신을 담당합니다.

package kr.co.petmates.api.bussiness.oauth.client;

import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoTokenResponse;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    private final RestTemplate restTemplate;

    public KakaoApiClient() {
        this.restTemplate = new RestTemplate();
    }

    // 인가코드로 엑세스토콘 요청하기
    public KakaoTokenResponse getAccessToken(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);
        params.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(kakaoTokenEndpoint, HttpMethod.POST, request, KakaoTokenResponse.class);

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
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // GET 요청 보내기
        ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
                kakaoUserInfoEndpoint,
                HttpMethod.GET,
                entity,
                KakaoUserInfoResponse.class
        );

        KakaoUserInfoResponse userInfo = response.getBody();

        logger.info("kakaoApiClient 사용자정보: {}", userInfo);
        return userInfo;


        // 방안1
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//
//
//        HttpEntity<?> userInfoEntity = new HttpEntity<>(headers);
//
//        // Post 방식으로 Http 요청
//        // 응답 데이터 형식은 Hashmap 으로 지정
//        ResponseEntity<KakaoUserInfoResponse> userResult = restTemplate.postForEntity(kakaoUserInfoEndpoint, userInfoEntity, KakaoUserInfoResponse.class);
////        Map<String, String> userResultMap = userResult.getBody();
//
//
////        HttpEntity<String> entity = new HttpEntity<>(headers);
////        ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(kakaoUserInfoEndpoint, HttpMethod.GET, entity, KakaoUserInfoResponse.class);
//        return userResult.getBody();
//
//
//----------
//
//        RestTemplate restTemplate = new RestTemplate();
//        String requestUrl = "https://kapi.kakao.com/v2/user/me";


//방안2
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "Bearer " + accessToken);
//
//        // postForObject를 사용하여 POST 요청
//        KakaoUserInfoResponse response = restTemplate.postForObject(kakaoUserInfoEndpoint, headers, KakaoUserInfoResponse.class);
//
//
//        logger.info("사용자정보: ", response);
//        return response;
    }
}
