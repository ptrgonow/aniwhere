package com.aniwhere.domain.user.join.service;

import com.aniwhere.domain.user.mapper.UserMapper;
import com.aniwhere.domain.user.join.domain.Join;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JoinService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;



    public void joinProcess(Join join) {




    }
}
