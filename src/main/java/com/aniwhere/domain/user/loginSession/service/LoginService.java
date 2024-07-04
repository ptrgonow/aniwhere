package com.aniwhere.domain.user.loginSession.service;

import com.aniwhere.domain.user.loginSession.domain.CustomUserDetails;
import com.aniwhere.domain.user.loginSession.domain.Login;
import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    private final UserMapper userMapper;

    @Autowired
    public LoginService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        LoginDTO loginDTO = userMapper.findByUsername(userId);
        if (loginDTO == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(
                loginDTO.getUserId(),
                loginDTO.getPassword(),
                loginDTO.getRole()
        );
    }

}
