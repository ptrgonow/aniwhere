package com.aniwhere.domain.user.loginSession.service;

import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.mapper.UserMngMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    private final UserMngMapper userMngMapper;

    @Autowired
    public LoginService(UserMngMapper userMngMapper) {
        this.userMngMapper = userMngMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginDTO loginDTO = userMngMapper.findByUsername(username);
        if (loginDTO == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(
                loginDTO.getUserName(),
                loginDTO.getPassword(),
                loginDTO.getRole()
        );
    }
}
