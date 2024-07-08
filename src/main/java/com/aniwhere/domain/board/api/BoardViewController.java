package com.aniwhere.domain.board.api;

import com.aniwhere.domain.board.notice.dto.NoticeDTO;
import com.aniwhere.domain.board.notice.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardViewController {

    private final NoticeService noticeService;

    public BoardViewController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/notice")
    public String notice(Model model) {
        List<NoticeDTO> notices = noticeService.getAllNotices();
        return "anitalk/notice"; // 공지사항 목록 페이지 템플릿
    }

    @GetMapping("/notice-detail")
    public String noticeDetail() {
        return "anitalk/notice-detail"; // 공지사항 상세 페이지 템플릿
    }

    @GetMapping("/notice-write")
    public String noticeWrite() {
        return "anitalk/notice-write"; // 공지사항 작성 페이지 템플릿
    }

    @GetMapping("/guide")
    public String guide() {
        return "anitalk/guide";
    }

    @GetMapping("/faq")
    public String faq() {
        return "anitalk/faq";
    }
}
