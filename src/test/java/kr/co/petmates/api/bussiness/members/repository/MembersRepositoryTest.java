//package kr.co.petmates.api.bussiness.members.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import kr.co.petmates.api.ApiApplication;
//import kr.co.petmates.api.bussiness.members.entity.Members;
//import kr.co.petmates.api.enums.Role;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ContextConfiguration;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
////@DataJpaTest
//@SpringBootTest
////@ActiveProfiles("test")
//@Testcontainers
//@DataJpaTest
//@ContextConfiguration(classes = ApiApplication.class) // @SEE: https://velog.io/@gillog/VSCode-jUnit-Test-실행-안될-때-Could-not-detect-default-configuration-classes-for-test-class
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // @SEE: https://charliezip.tistory.com/21
////public class MembersRepositoryTest extends ContainerTest {
//public class MembersRepositoryTest {
//
//    @Autowired
//    private MembersRepository membersRepository;
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Test
//    public void testMembersEntity() {
//        // given
////        String userPwd = passwordEncoder.encode("adminPwd");
////        String userPwd = passwordEncoder.encode("Rnfma1234!");
//        String userPwd = "Rnfma1234!";
////        String userPwd = passwordEncoder.encode("12351235");
//        Members members = Members.builder()
//                .id(2L)
//                .email("test@example.com")
//                .pwd(userPwd)
//                .nickname("testNickname")
//                .phone("01012345678")
//                .zipcode("12345")
//                .roadAddr("도로명주소")
//                .detailAddr("나머지주소")
//                .latitude("123.456")
//                .longitude("123.456")
//                .isWithdrawn(false)
//                .role(Role.ROLE_MEMBER)
//                .build();
//
//        // when
//        Members savedMembers = membersRepository.save(members);
//
//        // then
//        assertThat(savedMembers).isNotNull();
//        assertThat(savedMembers.getEmail()).isEqualTo("test@example.com");
//        System.out.println(userPwd);
//    }
//}
