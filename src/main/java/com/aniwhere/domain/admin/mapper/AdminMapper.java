package com.aniwhere.domain.admin.mapper;

import com.aniwhere.domain.admin.dto.MailDTO;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("SELECT id, user_id as userId, email, user_name as userName, address, detail_address as detailAddress, " +
            "zip_code as zipCode, phone, created_at as createdAt from user order by userId DESC LIMIT #{limit} OFFSET #{offset}")
    List<JoinDTO> selectAllUsers(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM user")
    int userCount();

    @Insert("INSERT INTO user_mail (title, content) VALUES (#{title}, #{content})")
    void insertMail(MailDTO mailDTO);

    @Select("SELECT email FROM user")
    List<String> selectAllUserEmails();

    @Select("SELECT id, user_id AS userId, email, user_name AS userName, address, detail_address AS detailAddress, phone, created_at AS createdAt " +
            "FROM user WHERE (detail_address IS NULL OR detail_address = '') OR (address IS NULL OR address = '') " +
            "ORDER BY user_id DESC LIMIT #{limit} OFFSET #{offset}")
    List<JoinDTO> emptyAdressUsers(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT id, user_id AS userId, email, user_name AS userName, address, detail_address AS detailAddress, phone, created_at AS createdAt " +
            "FROM user WHERE phone IS NULL ORDER BY user_id DESC LIMIT #{limit} OFFSET #{offset}")
    List<JoinDTO> emptyPhoneUsers(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM user WHERE (detail_address IS NULL OR detail_address = '') OR (address IS NULL OR address = '')")
    int countEmptyAddressUsers();

    @Select("SELECT COUNT(*) FROM user WHERE phone IS NULL")
    int countEmptyPhoneUsers();


}
