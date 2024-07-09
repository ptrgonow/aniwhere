package com.aniwhere.domain.board.notice.mapper;

import com.aniwhere.domain.board.notice.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import java.time.LocalDateTime;

@Mapper
public interface NoticeMapper {

    @Select("SELECT notice_id AS noticeId, title, content, created_at AS createdAt FROM notice ORDER BY notice_id DESC")
    List<NoticeDTO> getAllNotices();

    @Select("SELECT notice_id AS noticeId, title, content, created_at AS createdAt FROM notice WHERE notice_id = #{id}")
    NoticeDTO getNoticeById(Long id);

    @Insert("INSERT INTO notice (title, content, created_at) VALUES (#{title}, #{content}, NOW())")
    void insertNotice(NoticeDTO noticeDTO);

    @Update("UPDATE notice SET title = #{title}, content = #{content} WHERE notice_id = #{noticeId}")
    void updateNotice(NoticeDTO noticeDTO);
}



