package com.aniwhere.domain.user.mapper;

import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {

     @Insert("INSERT INTO user (user_id, user_pwd, user_name, email, address, detail_address, zip_code, phone, role) VALUES (#{userId}, #{userPwd}, #{userName}, #{email}, #{address}, #{detailAddress}, #{zipCode}, #{phone}, #{role})")
     void register(JoinDTO join);

     @Select("SELECT user_id AS userId, user_pwd AS userPwd, role FROM user WHERE user_id = #{username}")
     LoginDTO findByUserId(String username);
}


