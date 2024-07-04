package com.aniwhere.domain.board.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardViewController {

    @GetMapping("/notice")
    public String notice() {
        return "anitalk/notice";
    }

    @GetMapping("/notice-detail")
    public String noticeDetail() {
        return "anitalk/notice-detail";
    }

    @GetMapping("/notice-write")
    public String noticeWrite() {
        return "anitalk/notice-write";
    }

    @GetMapping("/guide")
    public String guide() {
        return "anitalk/guide";
    }



}
