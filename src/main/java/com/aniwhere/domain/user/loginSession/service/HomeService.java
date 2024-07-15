package com.aniwhere.domain.user.loginSession.service;

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

    // 인증된 사용자 이름 가져오기
    public String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            String userName = null;

            if (principal instanceof OAuth2User) {
                OAuth2User oauthUser = (OAuth2User) principal;
                Map<String, Object> attributes = oauthUser.getAttributes();
                String registrationId = oauthUser.getAuthorities().toString();

                if (registrationId.contains("ROLE_USER")) {
                    if (attributes.containsKey("response")) { // Naver
                        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                        userName = (String) response.get("name");
                    } else if (attributes.containsKey("kakao_account")) { // Kakao
                        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                        userName = (String) profile.get("nickname");
                    } else { // Other providers
                        userName = oauthUser.getAttribute("name");
                    }
                }
            } else if (principal instanceof UserDetails) {
                String userId = ((UserDetails) principal).getUsername();
                LoginDTO user = userMapper.findByUserId(userId);
                if (user != null) {
                    userName = user.getUserName();
                }
            }

            return userName;
        }
        return null;
    }

    // 인증된 사용자 ID 가져오기
    public String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            String userId = null;

            if (principal instanceof OAuth2User) {
                OAuth2User oauthUser = (OAuth2User) principal;
                Map<String, Object> attributes = oauthUser.getAttributes();
                String registrationId = oauthUser.getAuthorities().toString();

                if (registrationId.contains("ROLE_USER")) {
                    if (attributes.containsKey("response")) { // Naver
                        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                        userId = (String) response.get("id");
                    } else if (attributes.containsKey("kakao_account")) {// Kakao
                        Map<String, Object> kkoAttributes = oauthUser.getAttributes();
                        userId = String.valueOf(kkoAttributes.get("id"));
                    } else { // Other providers
                        userId = oauthUser.getAttribute("sub");
                    }
                }
            } else if (principal instanceof UserDetails) {
                userId = ((UserDetails) principal).getUsername();
            }

            return userId;
        }
        return null;
    }

}
