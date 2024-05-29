package com.aniwhere.domain.usermng.mapper;

import com.aniwhere.domain.usermng.loginSession.dto.LoginDTO;
import com.aniwhere.domain.usermng.register.dto.JoinDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMngMapper {


     LoginDTO findByUsername(@Param("username") String username);
     boolean checkByUserDatabase(@Param("username") String username);
     void insertUser(JoinDTO join);

}

