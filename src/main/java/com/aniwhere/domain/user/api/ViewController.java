package com.aniwhere.domain.user.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("usermngViewController")
@RequestMapping("/mng")
public class ViewController {

    @GetMapping("/runner")
    public String runner() {
        return "aniwhere/runner/runner-list";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "popup/mypage";
    }

    @GetMapping("/history")
    public String history() {
        return "popup/mypage-history";
    }


    @GetMapping("/guide")
    public String guide() {
        return "anitalk/guide";
    }
}
