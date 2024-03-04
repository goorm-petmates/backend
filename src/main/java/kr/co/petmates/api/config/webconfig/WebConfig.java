package kr.co.petmates.api.config.webconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*") // 프론트엔드 서버의 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowCredentials(true) // 인증 허용
                .allowedHeaders("Authorization", "Content-Type", "Refresh-Token"); // 요청 헤더 추가
    }
}