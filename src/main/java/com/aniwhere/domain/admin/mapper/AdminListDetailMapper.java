package com.aniwhere.domain.admin.mapper;

import com.aniwhere.domain.admin.dto.AdminListDetailDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminListDetailMapper {

    @Select("SELECT user_id AS userId, user_name AS userName, email, address, detail_address AS detailAddress, zip_code AS zipCode, phone, created_at AS createdAt FROM user WHERE role = #{role} LIMIT #{limit} OFFSET #{offset}")
    List<AdminListDetailDTO> selectAllAdmins(@Param("limit") int limit, @Param("offset") int offset, @Param("role") String role);

    @Select("SELECT COUNT(*) FROM user WHERE role = #{role}")
    int countAdmins(@Param("role") String role);

    @Select("SELECT user_id AS userId, user_name AS userName, email, address, detail_address AS detailAddress, zip_code AS zipCode, phone, created_at AS createdAt, role FROM user WHERE user_id = #{userId}")
    AdminListDetailDTO findAdminById(@Param("userId") String userId);

    @Select("SELECT user_id AS userId, user_name AS userName, email, address, detail_address AS detailAddress, zip_code AS zipCode, phone, created_at AS createdAt, role FROM user WHERE (user_id LIKE CONCAT('%', #{query}, '%') OR user_name LIKE CONCAT('%', #{query}, '%')) AND role = #{role} LIMIT #{limit} OFFSET #{offset}")
    List<AdminListDetailDTO> searchAdminByQuery(@Param("query") String query, @Param("limit") int limit, @Param("offset") int offset, @Param("role") String role);

    @Select("SELECT COUNT(*) FROM user WHERE (user_id LIKE CONCAT('%', #{query}, '%') OR user_name LIKE CONCAT('%', #{query}, '%')) AND role = #{role}")
    int countAdminsByQuery(@Param("query") String query, @Param("role") String role);

    @Update("UPDATE user SET user_name = #{userName}, email = #{email}, address = #{address}, detail_address = #{detailAddress}, zip_code = #{zipCode}, phone = #{phone}, role = #{role} WHERE user_id = #{userId}")
    int updateAdmin(AdminListDetailDTO admin);
}
