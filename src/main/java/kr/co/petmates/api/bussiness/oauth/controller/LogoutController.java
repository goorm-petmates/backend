package kr.co.petmates.api.bussiness.oauth.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.petmates.api.bussiness.oauth.service.LogoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/kakao") // 로그아웃 컨트롤러에도 기본 경로 설정
public class LogoutController {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    @Autowired
    private LogoutService logoutService;

    @PostMapping("/logout")
    public RedirectView logout(HttpSession session) {
        logoutService.logout(session);

        RedirectView redirectView = new RedirectView("http://localhost:3000/main");
        redirectView.setStatusCode(HttpStatus.FOUND); // 여기에서 상태 코드를 설정합니다.
        return redirectView;
//            , @RequestHeader("Authorization") String jwtToken, @RequestHeader("Refresh-Token") String refreshToken) {
//        jwtToken = jwtToken.replace("Bearer ", "");
//        logger.info("로그아웃 jwtToken: {}", jwtToken);
//        // 카카오 로그아웃 API 호출
////        logoutService.logoutKakao(jwtToken);
//        String email = jwtTokenProvider.getEmail(refreshToken);
//        Optional<User> user = userRepository.findByEmail(email);
//        String getRefreshToken
//        // refreshToken 만료 처리
//        logoutService.kakaoLogout(refreshToken);
//
//        RedirectView redirectView = new RedirectView("http://localhost:3000/main");
//        redirectView.setStatusCode(HttpStatus.FOUND); // 여기에서 상태 코드를 설정합니다.
//        return redirectView;
        // 처리 완료 후 리다이렉트
//        return new RedirectView("http://localhost:3000/main", true, HttpStatus.FOUND.value(), false);
    }
}
