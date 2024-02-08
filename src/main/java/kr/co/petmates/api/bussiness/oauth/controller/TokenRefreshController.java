// jwt 토큰 갱신 요청

package kr.co.petmates.api.bussiness.oauth.controller;

import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.oauth.service.TokenRefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/oauth/jwt") // 이 컨트롤러의 기본 경로를 "/api/oauth/jwt"로 설정합니다.
public class TokenRefreshController {
    @Autowired
    private TokenRefreshService tokenRefreshService; // 토큰 갱신 로직을 담당하는 서비스를 주입합니다.

    @PostMapping("/refresh") // "/api/oauth/jwt/refresh" 경로로 POST 요청을 받는 메소드를 정의합니다.
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        // 요청 본문에서 "refreshToken"을 추출하여 토큰 갱신 서비스를 호출합니다.
        String refreshToken = request.get("refreshToken");
        String newAccessToken = tokenRefreshService.refreshToken(refreshToken);

        // 새 액세스 토큰을 응답 본문에 포함하여 반환합니다.
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        // ResponseEntity 객체를 생성하여 newJwtToken 변수에 할당합니다.
        ResponseEntity<?> newJwtToken = ResponseEntity.ok(response);

        // newJwtToken 변수를 반환합니다.
        return newJwtToken;
    }
}
