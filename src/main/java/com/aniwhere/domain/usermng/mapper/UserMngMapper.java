package com.aniwhere.domain.usermng.mapper;

import com.aniwhere.domain.usermng.loginSession.domain.LoginSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMngMapper {

     LoginSession findByUsername(@Param("username") String username);
}

