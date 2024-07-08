package com.aniwhere.domain.board.notice.controller;

import com.aniwhere.domain.board.notice.dto.NoticeDTO;
import com.aniwhere.domain.board.notice.service.NoticeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoticeRestController {

    private final NoticeService noticeService;

    public NoticeRestController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 모든 공지사항 조회
    @GetMapping("/list")
    public ResponseEntity<List<NoticeDTO>> getNotices() {
        List<NoticeDTO> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    // 공지사항 수정
    @PutMapping("/update")
    public ResponseEntity<Void> updateNotice(@RequestBody NoticeDTO noticeDTO) {
        noticeService.updateNotice(noticeDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
