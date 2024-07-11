package com.aniwhere.domain.user.mypage.service;

import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.user.mapper.UserMapper;
import com.aniwhere.domain.user.mypage.domain.UserDetail;
import com.aniwhere.domain.user.mypage.dto.UpdateDetailDTO;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public boolean updateUser(UpdateDetailDTO userDetailDTO) {

        System.out.println("Update Request for User ID: " + userDetailDTO.getUserId());

        // 입력된 UserDetailDTO에서 userId가 null인지 확인
        if (userDetailDTO.getUserId() == null || userDetailDTO.getUserId().isEmpty()) {
            System.out.println("User ID is null or empty.");
            return false;
        }

        UserDetailDTO currentUserDetail = userMapper.detailByUserId(userDetailDTO.getUserId());

        // userId가 null인지 확인
        if (currentUserDetail == null) {
            System.out.println("No user found with the given User ID.");
            return false;
        }

        UpdateDetailDTO updateDetail = new UpdateDetailDTO();
        updateDetail.setUserId(userDetailDTO.getUserId());
        updateDetail.setUserPwd(userDetailDTO.getUserPwd());
        updateDetail.setUserName(userDetailDTO.getUserName());
        updateDetail.setEmail(userDetailDTO.getEmail());
        updateDetail.setAddress(userDetailDTO.getAddress());
        updateDetail.setDetailAddress(userDetailDTO.getDetailAddress());
        updateDetail.setZipCode(userDetailDTO.getZipCode());
        updateDetail.setPhone(userDetailDTO.getPhone());

        // 새로운 비밀번호가 입력되었는지 확인
        if (updateDetail.getNewUserPwd() != null && !updateDetail.getNewUserPwd().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updateDetail.getNewUserPwd());
            updateDetail.setUserPwd(encodedPassword);
        } else {
            // 기존 비밀번호를 유지
            updateDetail.setUserPwd(currentUserDetail.getUserPwd());
        }

        System.out.println("Updating user with ID: " + updateDetail.getUserId());

        return userMapper.updateUser(updateDetail);
    }

    public List<RouteDTO> selectRouteByUserId(String userId, int limit, int offset) {
        return userMapper.selectRouteByUserIdWithPaging(userId, limit, offset);
    }

    public int countUserRoutes(String userId) {
        return userMapper.countUserRoutes(userId);
    }

    public List<RouteDTO> selectRoutesWithMarkersByUserId(String userId) {
        return userMapper.selectRoutesWithMarkersByUserId(userId);
    }


}
