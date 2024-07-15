package com.aniwhere.domain.user.loginSession.service;

import com.aniwhere.domain.user.loginSession.domain.CustomUserDetails;
import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginDTO loginDTO = userMapper.findByUserId(username);

        if (loginDTO == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(
                loginDTO.getUserId(),
                loginDTO.getUserPwd(),
                loginDTO.getRole()
        );
    }

    public boolean authenticateUser(LoginDTO login) {
        try {
            UserDetails userDetails = loadUserByUsername(login.getUserId());
            return passwordEncoder.matches(login.getUserPwd(), userDetails.getPassword());
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }


}
