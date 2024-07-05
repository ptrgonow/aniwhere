package com.aniwhere.domain.user.mapper;

import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {

     @Insert("INSERT INTO user (user_id, user_pwd, user_name, email, address, detail_address, zip_code, phone, role, created_at ,is_social) VALUES (#{userId}, #{userPwd}, #{userName}, #{email}, #{address}, #{detailAddress}, #{zipCode}, #{phone}, #{role}, NOW(), #{isSocial})")
     void register(JoinDTO join);


     @Select("SELECT user_id AS userId, user_pwd AS userPwd, user_name AS userName, role FROM user WHERE user_id = #{username}")
     LoginDTO findByUserId(String username);

     @Select("SELECT user_id AS userId, user_pwd AS userPwd, user_name AS userName, email, address, detail_address AS detailAddress, zip_code AS zipCode, phone " +
             "FROM user WHERE user_id = #{userId}")
     UserDetailDTO detailByUserId(String userId);
}


