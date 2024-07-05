package com.aniwhere.domain.user.join.service;

import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class HomeService {

    private final UserMapper userMapper;

    public String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            String userId;
            String userName = null;

            if (principal instanceof OAuth2User) {
                OAuth2User oauthUser = (OAuth2User) principal;
                Map<String, Object> attributes = oauthUser.getAttributes();
                String registrationId = oauthUser.getAuthorities().toString();

                switch (registrationId) {
                    case "[ROLE_USER, SCOPE_profile, SCOPE_email, Naver]":
                        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                        userName = (String) response.get("name");
                        break;

                    case "[ROLE_USER, SCOPE_profile_nickname, SCOPE_account_email, Kakao]":
                        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                        userName = (String) profile.get("nickname");
                        break;

                    default:
                        userName = oauthUser.getAttribute("name");
                        break;
                }
            } else if (principal instanceof UserDetails) {
                userId = ((UserDetails) principal).getUsername();
                LoginDTO user = userMapper.findByUserId(userId);
                if (user != null) {
                    userName = user.getUserName();
                }
            }

            return userName;
        }
        return null;
    }
}
