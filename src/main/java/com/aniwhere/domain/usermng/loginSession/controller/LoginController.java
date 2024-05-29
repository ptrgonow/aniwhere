package com.aniwhere.domain.usermng.loginSession.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @GetMapping("/signin")
    public String loginPage() {
        return "popup/login";
    }

}
