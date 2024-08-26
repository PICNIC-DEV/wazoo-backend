package wazoo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wazoo.service.CustomUserDetailsService;
import wazoo.utils.CustomAccessDeniedHandler;
import wazoo.utils.CustomAuthenticationEntryPoint;
import wazoo.utils.JwtAuthFilter;
import wazoo.utils.JwtUtil;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    // API 엔드포인트와 Swagger 관련 요청을 허용할 화이트리스트
    private static final String[] AUTH_WHITELIST = {
            "/api/v1/users/**",
            "/api/v1/auth/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화
                .csrf(csrf -> csrf.disable())

                // CORS 설정을 기본값으로 사용
                .cors(withDefaults())

                // 세션 관리 상태를 'STATELESS'로 설정 (Spring Security가 세션을 생성하거나 사용하지 않음)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Form 로그인 비활성화 (기본 로그인 폼 사용하지 않음)
                .formLogin(form -> form.disable())

                // HTTP Basic 인증 비활성화 (기본 인증 방식 사용하지 않음)
                .httpBasic(withDefaults())

                // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class)

                // 예외 처리 설정
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                // 인증되지 않은 요청에 대한 처리
                                .authenticationEntryPoint(authenticationEntryPoint)
                                // 권한 부족에 대한 처리
                                .accessDeniedHandler(accessDeniedHandler))

                // 권한 규칙 설정
//                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

                .authorizeHttpRequests(authorize ->
                        authorize
                                // AUTH_WHITELIST에 포함된 요청은 모두 허용
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                // 나머지 요청은 인증 필요
                                .anyRequest().authenticated()
                );

        // SecurityFilterChain 객체를 반환하여 Spring Security 설정을 완료
        return http.build();
    }
}
