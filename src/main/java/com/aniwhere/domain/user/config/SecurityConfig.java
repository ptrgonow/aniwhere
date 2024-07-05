package com.aniwhere.domain.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // SecurityFilterChain : Spring Security 의 필터 연결을 구성하는 인터페이스
    // (시큐리티 6.0 이후 추가된 인터페이스, 이전에는 WebSecurityConfigurerAdapter 사용)
    // (람다식 '->' 사용 : 메소드가 하나일 때 사용 가능 / 람다식은 익명클래스를 간단하게 표현할 수 있게 해줌)
    // (익명 클래스 : 이름이 없는 클래스, 클래스를 정의하면서 동시에 객체를 생성하는 방법)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 요청 권한 인가
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/loginProc", "/joinProc", "/join").permitAll()
                        .requestMatchers("/resources/**", "/css/**", "/js/**", "/static/**","/img/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                // Form Login (사이트 자체 제작 로그인 페이지)
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .usernameParameter("userId")
                        .passwordParameter("userPwd")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                // OAuth2 Login (Naver, Kakao 같은 소셜 로그인 처리)
                // TODO : 아직 구현 안됨
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error=true")
                )

                // CSRF 토큰 비활성화 (CSRF : Cross Site Request Forgery - 사이트간 요청 위조)
                .csrf(csrf -> csrf.disable())

                // 로그아웃 설정 (세션 초기화, 쿠키 삭제)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true&message=logoutSuccess")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/error/403")
                );


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}
