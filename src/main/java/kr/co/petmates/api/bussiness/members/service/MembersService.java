package kr.co.petmates.api.bussiness.members.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.petmates.api.bussiness.members.dto.MembersDTO;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.oauth.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembersService {
    private final MembersRepository membersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Members getMemberInfoFromJwtToken(HttpServletRequest request) {
        String jwtToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                }
            }
        }

        if (jwtToken == null) {
            return null;
        }

        String email = jwtTokenProvider.getEmail(jwtToken);
        return membersRepository.findByEmail(email).orElse(null);
    }
    public Members editMemberInfo(MembersDTO membersDTO) {
        Members member = membersRepository.findByEmail(membersDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        member.setPhone(membersDTO.getPhone());
        member.setFullAddr(membersDTO.getFullAddr());
        member.setRoadAddr(membersDTO.getRoadAddr());
        member.setDetailAddr(membersDTO.getDetailAddr());
        member.setLatitude(membersDTO.getLatitude());
        member.setLongitude(membersDTO.getLongitude());
        member.setZipcode(membersDTO.getZipcode());

        return membersRepository.save(member);
    }
}
