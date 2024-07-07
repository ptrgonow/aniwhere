package com.aniwhere.domain.user.loginSession.controller;

import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import com.aniwhere.domain.user.loginSession.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final HomeService homeService;

    @PostMapping(value = "/loginProc", consumes = "application/x-www-form-urlencoded")
    public String loginProc(@RequestParam("userId") String userId,
                            @RequestParam("userPwd") String userPwd) {

        LoginDTO login = new LoginDTO();
        login.setUserId(userId);
        login.setUserPwd(userPwd);


        if (loginService.authenticateUser(login)) {
            return "redirect:/";
        } else {
            return "redirect:/login?error=true";
        }
    }


}
