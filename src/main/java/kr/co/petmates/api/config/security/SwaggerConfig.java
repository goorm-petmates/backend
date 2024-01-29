package kr.co.petmates.api.config.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "petmates API List & Test",
                description = "petmates 서비스 API 명세서 및 API 테스트",
                version = "v1"))
@Configuration
public class SwaggerConfig {
}
