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
        if (isDuplicate(join.getUserId(), join.getEmail(), join.getPhone(), join.getUserName())) {
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

        try {
            userMapper.register(joinDTO);
            return true; // 가입 성공
        } catch (Exception e) {
            System.out.println("Error occurred during registration: " + e.getMessage());
            return false; // 가입 실패
        }
    }

    public boolean isDuplicate(String userId, String email, String phone, String userName) {
        if (userId != null) {
            boolean exists = userMapper.existsByUserId(userId);
            if (exists) return true;
        }
        if (email != null) {
            boolean exists = userMapper.existsByEmail(email);
            if (exists) return true;
        }
        if (phone != null) {
            boolean exists = userMapper.existsByPhone(phone);
            if (exists) return true;
        }
        if (userName != null) {
            boolean exists = userMapper.existsName(userName);
            if (exists) return true;
        }
        return false;
    }

    public boolean isUserIdExists(String userId) {
        return userMapper.existsByUserId(userId);
    }

    public boolean isEmailExists(String email) {
        return userMapper.existsByEmail(email);
    }

    public boolean isPhoneExists(String phone) {
        return userMapper.existsByPhone(phone);
    }

    public boolean isUserNameExists(String userName) {
        return userMapper.existsName(userName);
    }
}
