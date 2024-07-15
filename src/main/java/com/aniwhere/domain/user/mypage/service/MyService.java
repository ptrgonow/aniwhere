package com.aniwhere.domain.user.mypage.service;

import com.aniwhere.domain.route.dto.MarkerDTO;
import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.route.mapper.RouteMapper;
import com.aniwhere.domain.user.mapper.UserMapper;
import com.aniwhere.domain.user.mypage.domain.UserDetail;
import com.aniwhere.domain.user.mypage.dto.UpdateDetailDTO;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@AllArgsConstructor
public class MyService {

    private final UserMapper userMapper;
    private final RouteMapper routeMapper;
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

    // 로그인 방식(소셜) 확인
    public boolean isSocialUser(String userId) {
        Integer isSocial = userMapper.getIsSocialByUserId(userId);
        return isSocial != null && isSocial == 1;
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
        UserDetailDTO currentUserDetail = userMapper.detailByUserId(userDetailDTO.getUserId());

        // 새로운 비밀번호가 입력되었는지 확인
        if (userDetailDTO.getNewUserPwd() != null && !userDetailDTO.getNewUserPwd().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userDetailDTO.getNewUserPwd());
            userDetailDTO.setUserPwd(encodedPassword);
        } else {
            // 기존 비밀번호를 유지
            userDetailDTO.setUserPwd(currentUserDetail.getUserPwd());
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

        return userMapper.updateUser(updateDetail) > 0;
    }

    // 이름, 핸드폰 중복 체크 로직
    public boolean existsByName(String userName, String userId) {
        return userMapper.existsByName(userName, userId);
    }

    public boolean existsByPhone(String phone, String userId){
        return userMapper.existsByUserPhone(phone, userId);
    }

    public String getUserPhone(String userId) {
        return userMapper.getUserPhone(userId);
    }

    public void deleteUser(String userId){
        userMapper.deleteUser(userId);
    }

    public List<RouteDTO> selectRouteByUserId(String userId, int limit, int offset) {
        return userMapper.selectRouteByUserIdWithPaging(userId, limit, offset);
    }

    public int countUserRoutes(String userId) {
        return userMapper.countUserRoutes(userId);
    }

    public RouteDTO getRouteDetailById(Long id) {
        RouteDTO route = routeMapper.selectRouteById(id);
        if (route != null) {
            List<MarkerDTO> markers = routeMapper.selectMarkersByRouteId(id);
            route.setMarkers(markers);
        }
        return route;
    }

    @Transactional
    public void updateRoute(RouteDTO routeDTO) {

        routeMapper.updateRoute(routeDTO.getId(), routeDTO);
        routeMapper.deleteMarkersByRouteId(routeDTO.getId());

        for (MarkerDTO marker : routeDTO.getMarkers()) {
            marker.setRouteId(routeDTO.getId());
            userMapper.insertMarker(marker);
        }

    }


    @Transactional
    public void deleteRoute(Long routeId) {
        // 마커 삭제
        routeMapper.deleteMarkersByRouteId(routeId);

        // 이미지 파일 삭제 로직 추가
        String imagePath = userMapper.findImageByRouteId(routeId);
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Files.deleteIfExists(Paths.get("src/main/resources/static" + imagePath));
            } catch (IOException e) {
                System.out.println("Error deleting image file: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // 경로 삭제
        userMapper.deleteRoute(routeId);
    }

}
