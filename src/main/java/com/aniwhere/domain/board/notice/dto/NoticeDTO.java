package com.aniwhere.domain.board.notice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDTO {

    private int noticeId;        // 공지사항 ID
    private String title;        // 제목
    private String content;      // 내용
    private LocalDateTime createdAt; // 작성 일시
    private int hit;  // 조회수
}

