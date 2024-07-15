package com.aniwhere.domain.user.mapper;

import com.aniwhere.domain.route.domain.Route;
import com.aniwhere.domain.route.dto.MarkerDTO;
import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.mypage.dto.UpdateDetailDTO;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {

     @Insert("INSERT INTO user (user_id, user_pwd, user_name, email, address, detail_address, zip_code, phone, role, created_at ,is_social) VALUES (#{userId}, #{userPwd}, #{userName}, #{email}, #{address}, #{detailAddress}, #{zipCode}, #{phone}, #{role}, NOW(), #{isSocial})")
     void register(JoinDTO join);

     @Select("SELECT user_id AS userId, user_pwd AS userPwd, user_name AS userName, role FROM user WHERE user_id = #{username}")
     LoginDTO findByUserId(String username);

     @Select("SELECT COUNT(*) > 0 FROM user WHERE user_id = #{userId}")
     boolean existsByUserId(String userId);

     @Select("SELECT COUNT(*) > 0 FROM user WHERE email = #{email}")
     boolean existsByEmail(String email);

     @Select("SELECT COUNT(*) > 0 FROM user WHERE phone = #{phone}")
     boolean existsByPhone(String phone);

     @Select("SELECT COUNT(*) > 0 FROM user WHERE user_name = #{userName}")
     boolean existsName(String userName);

     @Select("SELECT COUNT(*) FROM user WHERE phone = #{phone} AND user_id != #{userId}")
     boolean existsByUserPhone(@Param("phone") String phone, @Param("userId") String userId);

     @Select("SELECT COUNT(*) FROM user WHERE user_name = #{userName} AND user_id != #{userId}")
     boolean existsByName(@Param("userName") String userName, @Param("userId") String userId);

     @Select("SELECT phone FROM user WHERE user_id = #{userId}")
     String getUserPhone(String userId);

     @Select("SELECT is_social FROM user WHERE user_id = #{userId}")
     Integer getIsSocialByUserId(String userId);

     @Select("SELECT id, user_id AS userId, user_pwd AS userPwd, user_name AS userName, email, address, detail_address AS detailAddress, zip_code AS zipCode, phone " +
             "FROM user WHERE user_id = #{userId}")
     UserDetailDTO detailByUserId(@Param("userId") String userId);

     @Update("UPDATE user SET user_id = #{userId}, user_pwd = #{userPwd}, user_name = #{userName}, address = #{address}, detail_address = #{detailAddress}, zip_code = #{zipCode}, phone = #{phone} " +
             "WHERE user_id = #{userId}")
     int updateUser(UpdateDetailDTO updateDetail);

     @Delete("DELETE FROM user WHERE user_id = #{userId}")
     void deleteUser(String userId);

     @Select("SELECT id, name, description, created_at AS createdAt, user_id AS userId, image " +
             "FROM route WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
     List<RouteDTO> selectRouteByUserIdWithPaging(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);

     @Select("SELECT COUNT(*) FROM route WHERE user_id = #{userId}")
     int countUserRoutes(@Param("userId") String userId);

     @Insert("INSERT INTO marker (route_id, longitude, latitude) VALUES (#{routeId}, #{longitude}, #{latitude})")
     void insertMarker(MarkerDTO marker);

     @Delete("DELETE FROM route WHERE id = #{id}")
     void deleteRoute(Long id);

     @Select("SELECT image FROM route WHERE id = #{id}")
     String findImageByRouteId(@Param("id") Long id);


}


