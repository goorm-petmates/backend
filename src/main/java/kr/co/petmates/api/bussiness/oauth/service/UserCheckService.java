package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.Optional;
import kr.co.petmates.api.bussiness.members.dto.MembersDTO;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.oauth.controller.KakaoOauthController;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.enums.Role;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCheckService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    private final MembersRepository membersRepository;

    // isNewUser 값 반환, 이메일로 데이터베이스 조회(true: 신규, false: 기존)
    public boolean isNewUser(String email) {
        Optional<Members> memberOptional = membersRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            Members member = memberOptional.get();
            return member.getPhone() == null;
        }

        // 이메일로 사용자를 찾지 못한 경우에도 새로운 사용자로 간주
        return true;
    }

    // 사용자 존재 여부를 확인하고 업데이트하거나 새로 저장
    @Transactional
    public void saveOrUpdateUser(KakaoUserInfoResponse userInfo, HttpSession session) {
        logger.info("사용자 정보 저장");

        String email = userInfo.getEmail();
        Optional<Members> existingMember = membersRepository.findByEmail(email);

        if (existingMember.isPresent()) {
            // 데이터베이스에 이미 사용자가 있는 경우
            Members member = existingMember.get();
            member.setEmail(email);
            member.setNickname(userInfo.getNickname());
            member.setProfileImage(userInfo.getProfileImage());
            member.setKakaoId(userInfo.getKakaoId());
            // 기존 엔티티 업데이트
            membersRepository.save(member);
        } else {
            MembersDTO membersDTO = new MembersDTO();
            logger.info("신규 가입 이메일 저장:{}", userInfo.getEmail());

            // 넘겨받은 정보를 DTO에 저장
            membersDTO.setEmail(userInfo.getEmail());
            logger.info("임시 dto 저장 이메일: {}", membersDTO.getEmail());
            membersDTO.setKakaoId(userInfo.getKakaoId());
            logger.info("임시 dto 저장 KakaoId: {}", membersDTO.getKakaoId());
            membersDTO.setNickname(userInfo.getNickname());
            membersDTO.setProfileImage(userInfo.getProfileImage());
            membersDTO.setIsWithdrawn(false); // 기본값 설정
            if (userInfo.getEmail().equals("admin@petmates.co.kr")) {
                membersDTO.setRole(Role.ADMIN);
            } else {
                membersDTO.setRole(Role.USER);
            }

            // 세션에 DTO 저장
            session.setAttribute("tempUserInfo", membersDTO);
        }
    }
}
