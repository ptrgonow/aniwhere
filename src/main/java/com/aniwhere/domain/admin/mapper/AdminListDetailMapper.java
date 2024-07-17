package com.aniwhere.domain.admin.mapper;

import com.aniwhere.domain.admin.dto.AdminListDetailDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminListDetailMapper {

    @Select("SELECT id, user_id AS userId, user_name AS userName, email, address, detail_address AS detailAddress, zip_code AS zipCode, phone, role, created_at AS createdAt FROM user WHERE role = 'ROLE_ADMIN'")
    List<AdminListDetailDTO> selectAllAdmins();

    @Select("SELECT id, user_id AS userId, user_name AS userName, email, address, detail_address AS detailAddress, zip_code AS zipCode, phone, role, created_at AS createdAt FROM user WHERE user_id = #{userId} AND role = 'ROLE_ADMIN'")
    AdminListDetailDTO selectAdminById(String userId);

    @Update("UPDATE user SET user_name = #{userName}, email = #{email}, address = #{address}, detail_address = #{detailAddress}, zip_code = #{zipCode}, phone = #{phone} WHERE user_id = #{userId} AND role = 'ROLE_ADMIN'")
    int updateAdmin(AdminListDetailDTO admin);
}