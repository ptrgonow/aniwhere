package com.aniwhere.domain.user.register.service;

import com.aniwhere.domain.user.mapper.UserMngMapper;
import com.aniwhere.domain.user.register.domain.Join;
import com.aniwhere.domain.user.register.dto.JoinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserMngMapper userMngMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public JoinService(UserMngMapper userMngMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userMngMapper = userMngMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void joinProcess(Join join) {

        // 사용자 이름이 null 인지 확인
        if (join.getUserName() == null) {
            throw new IllegalArgumentException("유저 이름이 null 입니다.");
        }

        // 사용자 이름이 이미 존재하는지 확인
        if (userMngMapper.checkByUserDatabase(join.getUserName())) {
            throw new IllegalArgumentException("유저 이름이 이미 존재합니다.");
        }

        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setUserName(join.getUserName());
        joinDTO.setPassword(passwordEncoder.encode(join.getPassword()));
        joinDTO.setEmail(join.getEmail());
        joinDTO.setAddress(join.getAddress());
        joinDTO.setRole("ROLE_USER");

        userMngMapper.insertUser(joinDTO);
    }
}
