package com.aniwhere.domain.user.mapper;

import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMngMapper {


     LoginDTO findByUsername(String username);
}


