// Spring Data JPA에서 사용되며, 데이터베이스와 상호작용하기 위한 인터페이스로, Member 엔티티와 관련된 데이터 액세스 작업을 정의하고 실행

package kr.co.petmates.api.bussiness.oauth.repository;

import kr.co.petmates.api.bussiness.oauth.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 1. 카카오 계정 이메일로 회원 검색하여 신규 회원인지 아닌지 확인
    boolean existsByKakaoAccount(String kakaoAccount);

    // 2. 신규 회원인 경우, 회원 생성: members 테이블에 닉네임, 카카오 계정 저장
//    Member save(Member member);

    // 3. 기존 회원인 경우, 변경된 닉네임 또는 카카오 계정 업데이트
//    Member save(Member member);

}
