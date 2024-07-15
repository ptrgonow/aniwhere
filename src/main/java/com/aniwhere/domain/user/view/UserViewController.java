package com.aniwhere.domain.user.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login() {
        return "popup/login";
    }

    @GetMapping("/join")
    public String join() {
        return "popup/join";
    }

}
