package com.aniwhere.domain.usermng.loginSession.service;

import com.aniwhere.domain.usermng.loginSession.domain.LoginSession;
import com.aniwhere.domain.usermng.mapper.UserMngMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginSessionService implements UserDetailsService {

    private final UserMngMapper userMngMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginSessionService(UserMngMapper userMngMapper, PasswordEncoder passwordEncoder) {
        this.userMngMapper = userMngMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름으로 사용자 정보를 찾는 메소드
        LoginSession loginSession = userMngMapper.findByUsername(username);
        if (loginSession == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // 사용자 정보를 UserDetails로 변환하여 반환
        return org.springframework.security.core.userdetails.User
                .withUsername(loginSession.getUsername())
                .password(loginSession.getPassword())
                .roles(loginSession.getRole())
                .build();
    }

    public boolean authenticate(String username, String password) {
        // 사용자 인증 로직 (암호화 없이 평문으로 비교)
        LoginSession loginSession = userMngMapper.findByUsername(username);
        if (loginSession == null) {
            return false;
        }
        return password.equals(loginSession.getPassword());
    }

    public LoginSession findByUsername(String username) {
        // 사용자 이름으로 사용자 정보를 찾는 메소드
        return userMngMapper.findByUsername(username);
    }
}
