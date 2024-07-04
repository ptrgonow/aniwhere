package com.aniwhere.domain.user.loginSession.controller;

import com.aniwhere.domain.user.loginSession.domain.Login;
import com.aniwhere.domain.user.loginSession.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/loginProc")
    public String loginProc(Login login) {

        loginService.loadUserByUsername(login.getUserId());

        return "redirect:/";
    }

}
