package com.aniwhere.domain.board.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("boardViewController")
@RequestMapping("/board")
public class ViewController {

    @GetMapping("/faq")
    public String faq() {
        return "anitalk/faq";
    }

    @GetMapping("/notice")
    public String notice() {
        return "anitalk/notice";
    }

    @GetMapping("/notice-detail")
    public String noticeDetail() {
        return "anitalk/notice-detail";
    }

    @GetMapping("/guide")
    public String guide() {
        return "anitalk/guide";
    }

    @GetMapping("/write")
    public String noticeWrite() {
        return "anitalk/notice-write";
    }

    @GetMapping("/faq-write")
    public String faqWrite() {
        return "anitalk/faq-write";
    }


}
