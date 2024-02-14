package kr.co.petmates.api.bussiness.oauth.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCheckService {
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

//        boolean emailNotFound = !membersRepository.findByEmail(email).isPresent();
//        boolean phoneNotFound = !membersRepository.findByPhone(phone).isPresent();
//        return emailNotFound && phoneNotFound;
//        return !membersRepository.findByEmail(email).isPresent();
//    }

    // 사용자 존재 여부를 확인하고 업데이트하거나 새로 저장
    @Transactional
    public void saveOrUpdateUser(KakaoUserInfoResponse userInfo) {
        String email = userInfo.getEmail();
        Members members = membersRepository.findByEmail(email)
                .orElseGet(Members::new); // 기존 사용자가 없을 경우 새 Members 객체 생성

        // 사용자 정보를 업데이트하거나 설정합니다.
        members.setEmail(email);
        members.setKakaoId(userInfo.getKakaoId());
        members.setNickname(userInfo.getNickname());
        members.setProfileImage(userInfo.getProfileImage());
        members.setIsWithdrawn(false);
        members.setLastLoginDate(LocalDateTime.now());
        if (email.equals("admin@petmates.co.kr")) {
            members.setRole(Role.ADMIN);
        } else {
            members.setRole(Role.USER);
        }
        membersRepository.save(members);
    }
}
