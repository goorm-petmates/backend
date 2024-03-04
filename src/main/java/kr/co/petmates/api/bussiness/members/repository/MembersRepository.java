package kr.co.petmates.api.bussiness.members.repository;

import java.util.Optional;
import kr.co.petmates.api.bussiness.members.entity.Members;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembersRepository extends JpaRepository<Members, Long> {

//    @EntityGraph(attributePaths = "authorities")
    @EntityGraph(attributePaths = {"petsitter"})
    Optional<Members> findByEmail(String email);
    Optional<Members> findByNickname(String nickname);
}
