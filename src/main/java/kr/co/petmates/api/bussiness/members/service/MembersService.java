package kr.co.petmates.api.bussiness.members.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembersService {
//    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
//    private final MembersRepository membersRepository;
//    @Transactional
//    public void saveUser(KakaoUserInfoResponse userInfo, boolean isNewUser) {
//        logger.info("사용자 정보 저장");
//        String email = userInfo.getEmail();
//        Members members = membersRepository.findByEmail(email)
//                .orElseGet(Members::new); // 기존 사용자가 없을 경우 새 Members 객체 생성
//
//        String nickname = userInfo.getNickname();
//        boolean isNicknameDuplicate;
//        if (isNewUser) {
//            // 닉네임 중복 확인
//            isNicknameDuplicate = membersRepository.findByNickname(nickname).isPresent();
//            members.setNickname(isNicknameDuplicate ? null : nickname);
//        } else {
//            members.setNickname(userInfo.getNickname());
//        }
//
//        // 사용자 정보를 업데이트하거나 설정합니다.
//        members.setEmail(email);
//        members.setKakaoId(userInfo.getKakaoId());
//        logger.info("사용자정보 저장하기 kakaoId: {}", userInfo.getKakaoId());
//        members.setProfileImage(userInfo.getProfileImage());
//        members.setIsWithdrawn(false);
//        members.setLastLoginDate(LocalDateTime.now());
//        if (email.equals("admin@petmates.co.kr")) {
//            members.setRole(Role.ADMIN);
//        } else {
//            members.setRole(Role.USER);
//        }
//        membersRepository.save(members);
//        logger.info("사용자 정보 저장 완료");
//    }

//    @Transactional
//    public void saveUser(KakaoUserInfoResponse userInfo, boolean isNewUser) {
//        logger.info("사용자 정보 저장");
//        String email = userInfo.getEmail();
//        Members members = membersRepository.findByEmail(email)
//                .orElseGet(Members::new); // 기존 사용자가 없을 경우 새 Members 객체 생성
//
//        String nickname = userInfo.getNickname();
//        boolean isNicknameDuplicate;
//        if (isNewUser) {
//            // 닉네임 중복 확인
//            isNicknameDuplicate = membersRepository.findByNickname(nickname).isPresent();
//            members.setNickname(isNicknameDuplicate ? null : nickname);
//        } else {
//            members.setNickname(userInfo.getNickname());
//        }
//
//        // 사용자 정보를 업데이트하거나 설정합니다.
//        members.setEmail(email);
//        members.setKakaoId(userInfo.getKakaoId());
//        logger.info("사용자정보 저장하기 kakaoId: {}", userInfo.getKakaoId());
//        members.setProfileImage(userInfo.getProfileImage());
//        members.setIsWithdrawn(false);
//        members.setLastLoginDate(LocalDateTime.now());
//        if (email.equals("admin@petmates.co.kr")) {
//            members.setRole(Role.ADMIN);
//        } else {
//            members.setRole(Role.USER);
//        }
//        membersRepository.save(members);
//        logger.info("사용자 정보 저장 완료");
//    }
}
