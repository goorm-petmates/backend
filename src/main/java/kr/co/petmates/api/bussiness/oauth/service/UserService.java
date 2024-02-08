// 사용자정보로 데이터베이스 조회, isNewUse 체크
package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.oauth.client.KakaoApiClient;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import kr.co.petmates.api.bussiness.oauth.domain.User;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.bussiness.oauth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private KakaoApiClient kakaoApiClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCheckService userCheckService;

    @Autowired // 이 어노테이션은 생성자 기반 의존성 주입을 나타냅니다. Spring이 자동으로 UserRepository와 KakaoApiClient의 인스턴스를 주입합니다.
    public UserService(UserRepository userRepository, KakaoApiClient kakaoApiClient) {
        this.userRepository = userRepository;
        this.kakaoApiClient = kakaoApiClient;
    }

    public AuthResult createUserFromKakao(String accessToken) {
        // 카카오 API 클라이언트를 사용하여 사용자 정보를 가져옵니다. accessToken은 사용자 인증을 위해 필요합니다.
        KakaoUserInfoResponse kakaoUserInfo = kakaoApiClient.getUserInfo(accessToken);
        logger.info("userService 사용자정보: {}", kakaoUserInfo);

        String email = kakaoUserInfo.getEmail(); // 프로필에서 이메일 정보 추출
        logger.info("userService email값: {}", email);
        String nickname = kakaoUserInfo.getNickname(); // 프로필에서 이메일 정보 추출
        logger.info("userService nickname값: {}", nickname);

        // isNewUser 값 반환, 이메일로 데이터베이스 조회
        boolean isNewUser = userCheckService.isNewUser(email);
        logger.info("userService isNewUser값: {}", isNewUser);

        // 데이터베이스에 사용자 정보 저장 또는 업데이트
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> new User()); // 기존 사용자가 없을 경우 새 User 객체 생성
        user.setEmail(email); // 사용자의 이메일을 설정합니다.
        logger.info("userService 이메일: {}", email);
        user.setNickname(kakaoUserInfo.getNickname()); // 사용자의 닉네임을 설정합니다.
        logger.info("userService 닉네임: {}", kakaoUserInfo.getNickname());
        user.setProfileImage(kakaoUserInfo.getProfileImage()); // 사용자의 프로필 이미지 URL을 설정합니다.
        logger.info("userService 이메일: {}", kakaoUserInfo.getProfileImage());

        // UserCheckService를 사용하여 사용자 저장 또는 업데이트
        userCheckService.saveOrUpdateUser(user);

        String jwtToken = jwtTokenProvider.createJwtToken(email);
        logger.info("userService jwtToken값: {}", jwtToken);
        return new AuthResult(jwtToken, isNewUser);
    }

    // static 내부 클래스로 AuthResult 정의
    public static class AuthResult extends User {
        private final String jwtToken;
        private final boolean isNewUser;

        public AuthResult(String jwtToken, boolean isNewUser) {
            this.jwtToken = jwtToken;
            this.isNewUser = isNewUser;
        }

        public String getToken() {
            return jwtToken;
        }

        public boolean isNewUser() {
            return isNewUser;
        }

    }
}




// User 객체를 생성하고, 카카오 API 응답에서 받은 정보를 사용하여 초기화합니다.
//        User user = new User();
//        user.setNickname(kakaoUserInfo.getNickname()); // 사용자의 닉네임을 설정합니다.
//        user.setProfileImage(kakaoUserInfo.getProfileImage()); // 사용자의 프로필 이미지 URL을 설정합니다.
//        user.setAccountEmail(kakaoUserInfo.getAccountEmail()); // 사용자의 이메일을 설정합니다.
//
//        // UserRepository를 통해 User 객체를 데이터베이스에 저장합니다.
//        return userRepository.save(user);

// 사용자 정보를 데이터베이스에서 찾습니다.
//        Optional<User> existingUser = userRepository.findByEmail(accountEmail);
//        boolean isNewUser = !existingUser.isPresent(); // 신규회원 true, 기존회원이면 false
//        boolean isNewUser = !userRepository.findByEmail(accountEmail).isPresent();


//    public AuthResult authenticateUserFromKakao(String accessToken) {
//        // KakaoApiClient를 사용하여 카카오 서버로부터 사용자 정보 받아오기
//        KakaoUserInfoResponse userInfo = kakaoApiClient.getUserInfo(accessToken);
//
//
//        // 사용자 정보를 데이터베이스에서 찾습니다.
////        Optional<User> existingUser = userRepository.findByEmail(account_email);
////        boolean isNewUser = !existingUser.isPresent(); // 신규회원 true, 기존회원이면 false
//        String account_email = userInfo.getAccount_email(); // 프로필에서 이메일 정보 추출
//        boolean isNewUser = !userRepository.findByEmail(account_email).isPresent();
//
//        // 데이터베이스에 사용자 정보 저장 또는 업데이트
//        User user = userRepository.findByEmail(account_email)
//                .orElseGet(() -> new User()); // 기존 사용자가 없을 경우 새 User 객체 생성
//        user.setAccount_email(account_email); // User 객체에 이메일 설정
//        userRepository.save(user); // 데이터베이스에 사용자 정보 저장 또는 업데이트
//
//        String jwtToken = jwtTokenProvider.createJwtToken(account_email);
//        return new AuthResult(jwtToken, isNewUser);
//    }
//
//    // static 내부 클래스로 AuthResult 정의
//    public static class AuthResult {
//        private final String jwtToken;
//        private final boolean isNewUser;
//
//        public AuthResult(String jwtToken, boolean isNewUser) {
//            this.jwtToken = jwtToken;
//            this.isNewUser = isNewUser;
//        }
//
//        public String getToken() {
//            return jwtToken;
//        }
//
//        public boolean isNewUser() {
//            return isNewUser;
//        }
//    }
//}




    // 카카오 액세스 토큰을 사용하여 JWT 토큰을 생성하는 메소드
//    public boolean isNewUser(String accessToken) {
//        // KakaoApiClient를 통해 카카오 사용자 정보를 가져옵니다.
//        KakaoUserInfoResponse userInfo = kakaoApiClient.getUserInfo(accessToken);
//
//        // 카카오 사용자 정보에서 필요한 정보 추출
//        String account_email = userInfo.getAccount_email();
//        String nickname = userInfo.getNickname();
//        String profileImage = userInfo.getProfileImage();
//
//        // 사용자 정보를 데이터베이스에서 찾습니다.
//        Optional<User> existingUser = userRepository.findByEmail(account_email);
//        boolean isNewUser = !existingUser.isPresent(); // 신규회원 true, 기존회원이면 false
//
//        User user;
//        // 기존 회원 정보 업데이트, 추가 업데이트 필요한 정보가 있다면 여기에 추가
//        if (existingUser.isPresent()) {
//            user = existingUser.get();
//            user.setName(userInfo.getNickname());
//            user.setProfileImage(userInfo.getProfileImage());
//        } else {
//            // 신규 회원 정보 저장, 추가 필요한 정보 설정
//            user = new User();
//            user.setEmail(account_email);
//            user.setName(userInfo.getNickname());
//            user.setProfileImage(userInfo.getProfileImage());
//            isNewUser = true;
//        }
//        userRepository.save(user);
//
////        return jwtTokenProvider.createToken(user.getEmail(), isNewUser);
//        return isNewUser;
//    }
