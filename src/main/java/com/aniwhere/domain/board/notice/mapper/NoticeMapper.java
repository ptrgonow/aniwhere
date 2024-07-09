package com.aniwhere.domain.board.notice.mapper;

import com.aniwhere.domain.board.notice.dto.NoticeDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

import java.time.LocalDateTime;

@Mapper
public interface NoticeMapper {

    @Select("SELECT notice_id AS noticeId, title, content, created_at AS createdAt FROM notice ORDER BY notice_id DESC")
    List<NoticeDTO> getAllNotices();



    @Insert("INSERT INTO notice (title, content, created_at) VALUES (#{title}, #{content}, NOW())")
    void insertNotice(NoticeDTO noticeDTO);

    @Update("UPDATE notice SET title = #{title}, content = #{content} WHERE notice_id = #{noticeId}")
    void updateNotice(NoticeDTO noticeDTO);

    @Delete("DELETE FROM notice WHERE notice_id = #{noticeId}") // id -> noticeId
    void deleteNotice(int noticeId); // id -> noticeId

    @Select("SELECT notice_id AS noticeId, title, content, created_at AS createdAt, hit FROM notice WHERE notice_id = #{id}")
    NoticeDTO getNoticeById(Long id);

    @Update("UPDATE notice SET hit = hit + 1 WHERE notice_id = #{noticeId}")
    void incrementHit(Long noticeId);

}
