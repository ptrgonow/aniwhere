package com.aniwhere.domain.user.join.service;

import com.aniwhere.domain.user.join.dto.JoinDTO;
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

        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setUserId(join.getUserId());
        joinDTO.setUserPwd(passwordEncoder.encode(join.getUserPwd()));
        joinDTO.setUserName(join.getUserName());
        joinDTO.setEmail(join.getEmail());
        joinDTO.setAddress(join.getAddress());
        joinDTO.setDetailAddress(join.getDetailAddress());
        joinDTO.setZipCode(join.getZipCode());
        joinDTO.setPhone(join.getPhone());
        joinDTO.setRole("ROLE_USER");

        userMapper.register(joinDTO);



    }
}
