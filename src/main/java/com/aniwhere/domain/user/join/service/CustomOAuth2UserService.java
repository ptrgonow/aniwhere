package com.aniwhere.domain.user.join.service;

import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        String userid = null;
        String username = null;
        String email = null;
        String mobile = null;

        switch (registrationId) {
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                userid = (String) response.get("id");
                username = (String) response.get("name");
                email = (String) response.get("email");
                mobile = (String) response.get("mobile");
                break;

            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                userid = attributes.get("id").toString();
                username = (String) profile.get("nickname");
                email = (String) kakaoAccount.get("email");
                mobile = null;
                break;

            default:
                userid = oAuth2User.getAttribute(userNameAttributeName);
                username = oAuth2User.getAttribute("name");
                email = oAuth2User.getAttribute("email");
                mobile = oAuth2User.getAttribute("mobile");
                break;
        }

        LoginDTO user = userMapper.findByUserId(userid);
        if (user == null) {
            // 새로운 사용자 등록
            JoinDTO joinDTO = new JoinDTO();
            joinDTO.setUserId(userid);
            joinDTO.setUserPwd("");
            joinDTO.setUserName(username);
            joinDTO.setEmail(email);
            joinDTO.setAddress("");
            joinDTO.setDetailAddress("");
            joinDTO.setZipCode("");
            joinDTO.setPhone(mobile);
            joinDTO.setRole("ROLE_USER");
            joinDTO.setSocial(true);
            userMapper.register(joinDTO);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName
        );
    }
}
