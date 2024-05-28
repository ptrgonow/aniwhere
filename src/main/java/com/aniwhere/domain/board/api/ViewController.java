package com.aniwhere.domain.board.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("boardViewController")
@RequestMapping("/board")
public class ViewController {

    @GetMapping("/guide")
    public String guide() {
        return "anitalk/guide";
    }

}
