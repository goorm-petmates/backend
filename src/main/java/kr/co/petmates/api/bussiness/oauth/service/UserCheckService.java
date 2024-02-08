package kr.co.petmates.api.bussiness.oauth.service;

import kr.co.petmates.api.bussiness.oauth.domain.User;
import kr.co.petmates.api.bussiness.oauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCheckService {
    @Autowired
    private UserRepository userRepository;

    // isNewUser 값 반환, 이메일로 데이터베이스 조회
    public boolean isNewUser(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }

    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }
}
