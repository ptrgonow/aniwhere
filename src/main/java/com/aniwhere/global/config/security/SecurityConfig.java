package com.aniwhere.global.config.security;

import com.aniwhere.domain.usermng.loginSession.service.LoginSessionService;
import com.aniwhere.domain.usermng.mapper.UserMngMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/mng/signin", "/resources/**", "/css/**", "/js/**").permitAll() // 로그인 페이지, 리소스, 메인 페이지는 모든 사용자에게 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 페이지는 관리자만 접근 가능
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증된 사용자만 접근 가능
                )
                .formLogin(form -> form
                        .loginPage("/mng/signin") // 사용자 정의 로그인 페이지
                        .loginProcessingUrl("/login/session") // 로그인 폼 제출 URL
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 리디렉션할 URL
                        .failureUrl("/mng/signin?error=true") // 로그인 실패 시 리디렉션할 URL
                        .permitAll() // 로그인 페이지는 모든 사용자에게 허용
                )
                .logout(logout -> logout
                        .logoutUrl("/perform_logout") // 로그아웃 URL
                        .logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 시 리디렉션할 URL
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 평문 비밀번호 인코더를 사용하여 비밀번호를 암호화하지 않음
        return new PlainTextPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserMngMapper userMngMapper) {
        // 사용자 정보를 가져오는 서비스 로직을 UserDetailsService 인터페이스로 구현하여 빈으로 등록
        return new LoginSessionService(userMngMapper, passwordEncoder());
    }
}
