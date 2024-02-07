// 사용자 정보를 데이터베이스에 저장하고 관리, User 엔티티와 관련된 데이터베이스 액세스 작업을 정의하는 Spring Data JPA 리포지토리 인터페이스입니다.
package kr.co.petmates.api.bussiness.oauth.repository;

import kr.co.petmates.api.bussiness.oauth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 카카오 계정 이메일을 기반으로 사용자를 조회합니다.
    Optional<User> findByAccountEmail(String accountEmail);
}