package com.aniwhere.domain.user.join.service;

import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.user.mapper.UserMapper;
import com.aniwhere.domain.user.join.domain.Join;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JoinService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public boolean joinProcess(Join join) {
        if (isDuplicate(join.getUserId(), join.getEmail(), join.getPhone())) {
            return false; // 중복 항목 존재, 가입 불가
        }

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
        joinDTO.setSocial(false);

        userMapper.register(joinDTO);
        return true; // 가입 성공
    }

    public boolean isDuplicate(String userId, String email, String phone) {
        return userMapper.existsByUserId(userId) ||
                userMapper.existsByEmail(email) ||
                userMapper.existsByPhone(phone);
    }
}
