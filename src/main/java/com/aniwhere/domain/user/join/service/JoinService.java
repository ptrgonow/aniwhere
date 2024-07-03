package com.aniwhere.domain.user.join.service;

import com.aniwhere.domain.user.mapper.UserMngMapper;
import com.aniwhere.domain.user.join.domain.Join;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JoinService {

    private final UserMngMapper userMngMapper;
    private final BCryptPasswordEncoder passwordEncoder;



    public void joinProcess(Join join) {



    }
}
