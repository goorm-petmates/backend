// KakaoApiClient는 카카오 API와의 직접적인 통신을 담당합니다.

package kr.co.petmates.api.bussiness.oauth.client;

import org.springframework.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;
// 카카오 토큰 응답과 사용자 정보 응답을 처리하기 위한 DTO 클래스를 임포트합니다.
import kr.co.petmates.api.bussiness.oauth.dto.KakaoTokenResponse;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
// HTTP 통신에 필요한 스프링 클래스를 임포트합니다.
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
// 외부 설정값을 주입받기 위한 어노테이션을 임포트합니다.
import org.springframework.beans.factory.annotation.Value;

@Component
public class KakaoApiClient {

    // application.yaml에서 kakao.client-id 값을 주입받습니다.
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    // 토큰 발급 엔드포인트 URL을 설정 파일에서 주입받습니다.
    @Value("${kakao.token-endpoint}")
    private String kakaoTokenEndpoint;

    // 사용자 정보 조회 엔드포인트 URL을 설정 파일에서 주입받습니다.
    @Value("${kakao.userinfo-endpoint}")
    private String kakaoUserInfoEndpoint;

    // REST API 호출을 위한 RestTemplate 객체를 선언합니다.
    private final RestTemplate restTemplate;

    public KakaoApiClient() {
        // RestTemplate 객체를 초기화합니다.
        this.restTemplate = new RestTemplate();
    }

    // 인가 코드를 사용하여 액세스 토큰을 요청하는 메소드
    public KakaoTokenResponse getAccessToken(String authorizationCode) {
        Map<String, String> params = new HashMap<>(); // 요청 파라미터를 담을 맵을 생성합니다.
        params.put("grant_type", "authorization_code"); // 인증 코드 그랜트 타입을 설정합니다.
        params.put("client_id", clientId); // 카카오 앱의 클라이언트 ID를 설정 파일에서 가져온 값으로 설정합니다.
        params.put("redirect_uri", redirectUri); // 카카오 앱에 설정된 리디렉션 URI를 설정 파일에서 가져온 값으로 설정합니다.
        params.put("code", authorizationCode); // 인가 코드를 파라미터로 추가합니다.

        HttpHeaders headers = new HttpHeaders(); // 요청 헤더를 생성합니다.
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 컨텐트 타입을 application/x-www-form-urlencoded로 설정합니다.

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers); // 요청 엔티티를 생성합니다.
        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(kakaoTokenEndpoint, request, KakaoTokenResponse.class); // POST 요청을 통해 토큰을 요청하고 응답을 받습니다.

        return response.getBody(); // 응답 본문을 KakaoTokenResponse 객체로 반환합니다.
    }

    // 액세스 토큰을 사용하여 사용자 정보를 요청하는 메소드입니다.
    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders(); // 요청에 사용할 HTTP 헤더 객체를 생성합니다.
        headers.set("Authorization", "Bearer " + accessToken); // 'Authorization' 헤더에 액세스 토큰을 'Bearer' 타입으로 설정합니다.
        HttpEntity<String> entity = new HttpEntity<>(headers); // 생성한 헤더를 사용하여 HttpEntity 객체를 생성합니다.

        // restTemplate을 사용하여 HTTP GET 요청을 kakaoUserInfoEndpoint에 보내고, KakaoUserInfoResponse 타입으로 응답을 받습니다.
        ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(kakaoUserInfoEndpoint, HttpMethod.GET, entity, KakaoUserInfoResponse.class);
        return response.getBody(); // 받은 응답에서 본문을 추출하여 반환합니다.
    }
}
