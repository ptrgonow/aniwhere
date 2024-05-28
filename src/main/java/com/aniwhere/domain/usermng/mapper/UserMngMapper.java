package com.aniwhere.domain.usermng.mapper;

import com.aniwhere.domain.usermng.loginSession.domain.LoginSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMngMapper {

    LoginSession findByUsername(String username); // 사용자명을 통해 사용자 정보 조회

}
