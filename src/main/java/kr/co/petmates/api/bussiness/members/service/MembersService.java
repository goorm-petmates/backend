package kr.co.petmates.api.bussiness.members.service;

import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembersService {
    private MembersRepository membersRepository;

    public Members getMemberByEmail(String email) {
        return membersRepository.findByEmail(email).orElse(null);
    }

    public void saveMember(Members member) {
        membersRepository.save(member);
    }
}
