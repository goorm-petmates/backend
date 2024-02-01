// KakaoOauthController는 사용자의 인가 코드를 처리하고, JWT 토큰을 생성하여 반환하는 컨트롤러입니다.
package kr.co.petmates.api.bussiness.oauth.controller;

import kr.co.petmates.api.bussiness.oauth.service.KakaoOauthService;
import kr.co.petmates.api.bussiness.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 이 클래스를 REST 컨트롤러로 선언합니다.
@RequestMapping("/api/kakao") // 이 컨트롤러의 모든 매핑은 "/api/kakao" 경로로 시작합니다.
public class KakaoOauthController {

    @Autowired // 스프링의 의존성 주입 기능을 사용하여 KakaoOauthService 객체를 자동으로 주입합니다.
    private KakaoOauthService kakaoOauthService;

    @Autowired // UserService 객체를 자동으로 주입합니다.
    private UserService userService;

    // @GetMapping("/login") // GET 방식의 "/login" 경로로 매핑되는 메소드입니다.
    //    public ResponseEntity<?> kakaoLogin() { // 임시로 인가 코드 없이 호출할 수 있는 메소드입니다.
    //    System.out.println("apple"); // 콘솔에 "apple"을 출력합니다.
    //    return ResponseEntity.ok("{login : user}"); // 간단한 문자열을 응답으로 반환합니다.

    @PostMapping("/login") // "/login" 경로로 POST 요청이 오면 이 메소드를 실행합니다.
    public ResponseEntity<?> kakaoLogin(@RequestBody String authorizationCode) { // 클라이언트로부터 받은 인가 코드를 매개변수로 받습니다.
        // 인가 코드를 사용하여 액세스 토큰을 요청합니다.
        String accessToken = kakaoOauthService.getAccessToken(authorizationCode);
        // 받은 액세스 토큰으로 JWT 토큰을 생성합니다.
        String jwtToken = userService.createJwtToken(accessToken);
        // 액세스 토큰을 사용하여 사용자 정보를 요청하고 결과를 가져옵니다.
//        KakaoUserInfoResponse userInfo = kakaoOauthService.getUserInfo();

        // 프론트엔드 URL에 JWT 토큰을 쿼리 파라미터로 추가하여 리다이렉트합니다.
        String redirectUrl = "http://localhost:3000/oauth/token/kakao?jwtToken=" + jwtToken;
        // HttpHeaders 객체를 생성합니다. 이 객체를 사용하여 HTTP 응답에 헤더를 추가할 수 있습니다.
        HttpHeaders headers = new HttpHeaders();
        // 'Location' 헤더에 리다이렉션할 URL을 추가합니다. 이 헤더는 클라이언트에게 새로운 위치로 이동하라는 지시를 담고 있습니다.
        headers.add("Location", redirectUrl);
        // ResponseEntity 객체를 생성하여 반환합니다. 이 객체는 설정한 헤더와 HTTP 상태 코드(여기서는 302 Found, 리다이렉션을 의미)를 함께 클라이언트에 전달합니다.

        return new ResponseEntity<>(headers, HttpStatus.FOUND);


        // 프론트엔드에 반환할 JWT 토큰과 사용자 정보를 Map에 담습니다.
//        Map<String, Object> response = new HashMap<>();
//        response.put("jwtToken", jwtToken); // JWT 토큰을 Map에 추가합니다.
//        response.put("userInfo", userInfo); // 사용자 정보를 Map에 추가합니다.

        // 생성된 JWT 토큰을 응답으로 반환합니다. (응답 구성을 개선할 필요가 있습니다)
//        return ResponseEntity.ok(jwtToken);
    }
}