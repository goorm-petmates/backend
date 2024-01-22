package kr.co.petmates.api;

import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Disabled
@ContextConfiguration(classes = ApiApplicationTests.class)
@ActiveProfiles("test")
public abstract class ContainerTest {
}
