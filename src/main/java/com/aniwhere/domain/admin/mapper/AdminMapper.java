package com.aniwhere.domain.admin.mapper;

import com.aniwhere.domain.user.join.dto.JoinDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("SELECT id, user_id as userId, email,user_name as userName, address, detail_address as detailAddress, " +
            "zip_code as zipCode, phone, created_at as createdAt from user order by userId")
    public List<JoinDTO> selectAllUsers();
}
