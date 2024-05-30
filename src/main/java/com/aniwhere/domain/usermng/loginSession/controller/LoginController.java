package com.aniwhere.domain.usermng.loginSession.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "popup/login";
    }

}
