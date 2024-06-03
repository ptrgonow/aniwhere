package com.aniwhere.domain.user.mapper;

import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.register.dto.JoinDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMngMapper {


     LoginDTO findByUsername(@Param("username") String username);
     boolean checkByUserDatabase(@Param("username") String username);
     void insertUser(JoinDTO join);

}

