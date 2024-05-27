package com.aniwhere.domain.board.notice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

    @GetMapping("/notice")
    public String noticeList() {
        return "board/notice/notice_list";
    }

}
