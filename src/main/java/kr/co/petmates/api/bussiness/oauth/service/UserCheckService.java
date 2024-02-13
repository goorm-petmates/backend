package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.oauth.dto.KakaoUserInfoResponse;
import kr.co.petmates.api.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCheckService {
    @Autowired
    private MembersRepository membersRepository;

    // isNewUser 값 반환, 이메일로 데이터베이스 조회
    public boolean isNewUser(String email) {
//    public boolean isNewUser(String email, String phone) {
//        boolean emailNotFound = !membersRepository.findByEmail(email).isPresent();
//        boolean phoneNotFound = !membersRepository.findByPhone(phone).isPresent();
//        return emailNotFound && phoneNotFound;
        return !membersRepository.findByEmail(email).isPresent();
    }

    // 사용자 존재 여부를 확인하고 업데이트하거나 새로 저장
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
        if (email.equals("admin@petmates.co.kr")) {
            members.setRole(Role.ADMIN);
        } else {
            members.setRole(Role.USER);
        }
        membersRepository.save(members);
    }
}
