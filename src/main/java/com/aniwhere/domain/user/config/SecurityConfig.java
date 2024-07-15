package com.aniwhere.domain.user.config;

import com.aniwhere.domain.user.join.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/loginProc", "/joinProc", "/join", "/user/exists", "/user/joinProc", "/user/sendemail").permitAll()
                        .requestMatchers("/resources/**", "/css/**", "/js/**", "/static/**", "/img/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .usernameParameter("userId")
                        .passwordParameter("userPwd")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error=true")
                                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                        .userService(customOAuth2UserService)
                                )
                                .tokenEndpoint(tokenEndpoint ->
                                        tokenEndpoint
                                                .accessTokenResponseClient(kakaoAccessTokenResponseClient())
                                )
                )
                .csrf(csrf -> csrf.disable())

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

    @Bean
    public DefaultAuthorizationCodeTokenResponseClient kakaoAccessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        tokenResponseClient.setRequestEntityConverter(new KakaoOAuth2AccessTokenRequestEntityConverter());
        return tokenResponseClient;
    }

    private static class KakaoOAuth2AccessTokenRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
        @Override
        public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
            formParameters.add(OAuth2ParameterNames.GRANT_TYPE, authorizationCodeGrantRequest.getGrantType().getValue());
            formParameters.add(OAuth2ParameterNames.CODE, authorizationCodeGrantRequest.getAuthorizationExchange().getAuthorizationResponse().getCode());
            formParameters.add(OAuth2ParameterNames.REDIRECT_URI, authorizationCodeGrantRequest.getAuthorizationExchange().getAuthorizationRequest().getRedirectUri());
            formParameters.add(OAuth2ParameterNames.CLIENT_ID, authorizationCodeGrantRequest.getClientRegistration().getClientId());
            formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, authorizationCodeGrantRequest.getClientRegistration().getClientSecret());

            return new RequestEntity<>(formParameters, headers, HttpMethod.POST, URI.create(authorizationCodeGrantRequest.getClientRegistration().getProviderDetails().getTokenUri()));
        }
    }

}
