package com.aniwhere.domain.board.view;

import com.aniwhere.domain.board.notice.dto.NoticeDTO;
import com.aniwhere.domain.board.notice.service.NoticeService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardViewController {

    private final NoticeService noticeService;
    private final HomeService homeService;

    @GetMapping("/notice")
    public String notice(Model model) {
        List<NoticeDTO> notices = noticeService.getAllNotices();
        model.addAttribute("notices", notices);
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "anitalk/notice"; // 공지사항 목록 페이지 템플릿
    }

    @GetMapping("/notice-detail")
    public String noticeDetail(@RequestParam("id") Long id, Model model) {
        NoticeDTO notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "anitalk/notice-detail"; // 공지사항 상세 페이지 템플릿
    }

    @GetMapping("/notice-write")
    public String noticeWrite(Model model) {
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
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
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "anitalk/notice-update"; // 공지사항 수정 페이지 템플릿
    }

    @PostMapping("/update-notice")
    public String updateNotice(NoticeDTO noticeDTO, Model model) {
        noticeService.updateNotice(noticeDTO);
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "redirect:/board/notice"; // 수정 후 목록으로 리다이렉트
    }

    @GetMapping("/guide")
    public String guide(Model model) {
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "anitalk/guide";
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "anitalk/faq";
    }
}
