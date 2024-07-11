package com.aniwhere.domain.board.api;

import com.aniwhere.domain.board.notice.dto.NoticeDTO;
import com.aniwhere.domain.board.notice.service.NoticeService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("notices", notices);
        return "anitalk/notice"; // 공지사항 목록 페이지 템플릿
    }

    @GetMapping("/notice-detail")
    public String noticeDetail(@RequestParam("id") Long id, Model model) {
        NoticeDTO notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "anitalk/notice-detail"; // 공지사항 상세 페이지 템플릿
    }

    @GetMapping("/notice-write")
    public String noticeWrite() {
        return "anitalk/notice-write"; // 공지사항 작성 페이지 템플릿
    }

    @PostMapping("/submit-notice")
    public String submitNotice(NoticeDTO noticeDTO) {
        noticeService.createNotice(noticeDTO);
        return "redirect:/board/notice"; // 작성 후 목록으로 리다이렉트
    }

    @GetMapping("/notice-update")
    public String getNoticeUpdatePage(@RequestParam("id") Long id, Model model) {
        NoticeDTO notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "anitalk/notice-update"; // 공지사항 수정 페이지 템플릿
    }

    @PostMapping("/update-notice")
    public String updateNotice(NoticeDTO noticeDTO) {
        noticeService.updateNotice(noticeDTO);
        return "redirect:/board/notice"; // 수정 후 목록으로 리다이렉트
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
