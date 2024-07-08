package com.aniwhere.domain.board.notice.mapper;

import com.aniwhere.domain.board.notice.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

@Mapper
public interface NoticeMapper {
    @Select("SELECT notice_id AS noticeId, title, content, created_at AS createdAt FROM notice ORDER BY created_at DESC")
    List<NoticeDTO> getAllNotices();

    @Insert("INSERT INTO notice (title, content, created_at) VALUES (#{title}, #{content}, NOW())")
    void insertNotice(NoticeDTO noticeDTO);
}


