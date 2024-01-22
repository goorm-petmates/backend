package kr.co.petmates.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@SpringBootTest
class ApiApplicationTests {

	//mariadb container instance 생성
	@Bean
	@ServiceConnection
	MySQLContainer<?> mySQLContainer() {
		return new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"));
	}

	public static void main(String[] args) {
		SpringApplication.from(ApiApplication::main).with(ApiApplicationTests.class).run(args);
	}

	@Test
	void contextLoads() {
	}

}
