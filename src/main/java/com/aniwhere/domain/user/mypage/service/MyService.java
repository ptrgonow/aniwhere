package com.aniwhere.domain.user.mypage.service;

import com.aniwhere.domain.user.mapper.UserMapper;
import com.aniwhere.domain.user.mypage.domain.UserDetail;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDetail getUserDetailByUserId(String userId) {
        UserDetailDTO userDetail = userMapper.detailByUserId(userId);
        return convertToDomain(userDetail);
    }

    public UserDetail convertToDomain(UserDetailDTO userDetail){

        UserDetail detailDto = new UserDetail();
        detailDto.setId(userDetail.getId());
        detailDto.setUserId(userDetail.getUserId());
        detailDto.setUserPwd(passwordEncoder.encode(userDetail.getUserPwd()));
        detailDto.setUserName(userDetail.getUserName());
        detailDto.setEmail(userDetail.getEmail());
        detailDto.setAddress(userDetail.getAddress());
        detailDto.setDetailAddress(userDetail.getDetailAddress());
        detailDto.setZipCode(userDetail.getZipCode());
        detailDto.setPhone(userDetail.getPhone());

        return detailDto;
    }

    public boolean checkPassword(String userId, String userPwd) {
        UserDetailDTO userDetailDTO = userMapper.detailByUserId(userId);
        if (userDetailDTO != null) {
            return passwordEncoder.matches(userPwd, userDetailDTO.getUserPwd());
        }
        return false;
    }


}
