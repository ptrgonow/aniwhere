package com.aniwhere.domain.user.mypage.service;

import com.aniwhere.domain.user.mapper.UserMapper;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyService {

    private final UserMapper userMapper;

    public UserDetailDTO getUserDetailByUserId(String userId) {
        UserDetailDTO userDetail = userMapper.detailByUserId(userId);
        return convertToDTO(userDetail);
    }

    public UserDetailDTO convertToDTO(UserDetailDTO userDetail){

        UserDetailDTO detailDto = new UserDetailDTO();
        detailDto.setId(userDetail.getId());
        detailDto.setUserId(userDetail.getUserId());
        detailDto.setUserPwd(userDetail.getUserPwd());
        detailDto.setUserName(userDetail.getUserName());
        detailDto.setEmail(userDetail.getEmail());
        detailDto.setAddress(userDetail.getAddress());
        detailDto.setDetailAddress(userDetail.getDetailAddress());
        detailDto.setZipCode(userDetail.getZipCode());
        detailDto.setPhone(userDetail.getPhone());

        return detailDto;
    }




}
