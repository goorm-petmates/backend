package kr.co.petmates.api.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import kr.co.petmates.api.config.jwt.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
//@EnableGlobalAuthentication(prePostEnable = true)
@RequiredArgsConstructor
public class SecurityConfig {
//    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    // PasswordEncoder는 BCryptPasswordEncoder를 사용
    @Bean
    @Qualifier("bcryptPasswordEncoder")
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling((exceptionConfig) ->
//                        exceptionConfig.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler)
//                )
                .headers(headers -> headers
                        .frameOptions(FrameOptionsConfig::disable) // H2 콘솔 프레임 관련 보안 비활성화
                )
                // 세션을 사용하지 않기 때문에 SessionCreationPolicy.STATELESS로 설정함
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
//                .cors(Customizer.withDefaults()) // 기본 CORS 설정 사용
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 커스텀 CORS 설정 적용
//                .httpBasic(withDefaults())
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/h2-console/**", "/favicon.ico").permitAll() // H2 콘솔, favicon 접근 허용
                                .requestMatchers("/favicon.ico").permitAll() // favicon 접근 허용
                                .requestMatchers(PathRequest.toH2Console()).permitAll()// favicon.ico 요청 인증 무시
                                .requestMatchers("/api/kakao/**", "/api/oauth/**", "/api/members/test","/api/my-page/**",
                                        "/v3/**", "/swagger-ui/**", "/api/reserve/**",
                                        "/api/members/login",
                                        "/api/petsitter/**").permitAll() // 접근 허용
                                .requestMatchers("/api/members/my-info", "/api/members/add-image", "/api/members/delete",
                                        "/api/members/logout", "/adm/test",
                                        "/api/ide/**", "/api/chat/**", "/chat/**").authenticated() // 로그인했을 때 접근 허용
                                .requestMatchers(PathRequest.toH2Console()).permitAll() // H2 콘솔접근 허용
                                .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
//                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class) // JwtFilter 추가
                // 'apply(C)' is deprecated since version 6.2 and marked for removal
//                .apply(new JwtSecurityConfig(tokenProvider)) // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용
                .formLogin(
                        form -> form.disable()
                )
//                .logout(logout -> logout
//                        .logoutUrl("/members/logout") // 로그아웃 URL 설정
//                        .permitAll()
//                );
        ;
        return httpSecurity.build();
    }


    // CORS 설정을 위한 Bean 정의
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // 실제 환경에서는 구체적으로 지정
//        configuration.setAllowedOrigins(Arrays.asList("https://example.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
