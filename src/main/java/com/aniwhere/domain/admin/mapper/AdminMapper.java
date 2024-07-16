package com.aniwhere.domain.admin.mapper;

import com.aniwhere.domain.admin.dto.MailDTO;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {

    // @Select: 실행할 SQL SELECT 쿼리를 정의하는 어노테이션입니다.
    //"SELECT id, user_id as userId, email, user_name as userName, ..." - user 테이블에서 데이터를 조회하는 SQL 쿼리입니다.
    // 이 쿼리는 userId를 기준으로 정렬하여 모든 사용자 정보를 조회합니다.
    //public List<JoinDTO> selectAllUsers(); - 쿼리 실행 결과를 JoinDTO 객체의 리스트로 반환합니다.
    @Select("SELECT id, user_id as userId, email, user_name as userName, address, detail_address as detailAddress, " +
            "zip_code as zipCode, phone, created_at as createdAt from user order by userId")
    public List<JoinDTO> selectAllUsers();

    // @Insert: 실행할 SQL INSERT 쿼리를 정의하는 어노테이션입니다.
    //"INSERT INTO user_mail (title, content) VALUES (#{title}, #{content})" - user_mail 테이블에 새로운 이메일 레코드를 삽입하는 SQL 쿼리입니다. MailDTO 객체의 title과 content 필드 값이 쿼리의 VALUES 부분에 삽입됩니다.
    //void insertMail(MailDTO mailDTO); - MailDTO 객체를 인자로 받아서 SQL INSERT 쿼리를 실행합니다.
    @Insert("INSERT INTO user_mail (title, content) VALUES (#{title}, #{content})")
    void insertMail(MailDTO mailDTO);

    // @Select: 실행할 SQL SELECT 쿼리를 정의하는 어노테이션입니다.
    //"SELECT email FROM user" - user 테이블에서 모든 이메일 주소를 조회하는 SQL 쿼리입니다.
    //List<String> selectAllUserEmails(); - 쿼리 실행 결과를 String 타입의 리스트로 반환합니다. 즉, 모든 사용자 이메일 주소를 리스트 형태로 반환합니다.
    @Select("SELECT email FROM user")
    List<String> selectAllUserEmails();
}



