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

    // 공지사항 상세 조회 및 조회수 증가
    @GetMapping("/detail")
    public ResponseEntity<NoticeDTO> getNoticeDetail(@RequestParam("id") Long noticeId) {
        NoticeDTO notice = noticeService.getNoticeById(noticeId);
        if (notice != null) {
            return ResponseEntity.ok(notice);
        } else {
            return ResponseEntity.notFound().build();  // 공지사항이 없을 경우 404 응답
        }
    }

    // 공지사항 수정
    @PutMapping("/update")
    public ResponseEntity<Void> updateNotice(@RequestBody NoticeDTO noticeDTO) {
        noticeService.updateNotice(noticeDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 삭제
    @DeleteMapping("/board/delete-notice")
    public ResponseEntity<String> deleteNotice(@RequestParam("id") Long noticeId) {
        try {
            noticeService.deleteNotice(noticeId.intValue());
            return ResponseEntity.ok("삭제가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
        }
    }



}