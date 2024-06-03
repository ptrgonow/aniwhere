package com.aniwhere.domain.user.register.controller;

import com.aniwhere.domain.user.register.domain.Join;
import com.aniwhere.domain.user.register.service.JoinService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@Controller("JoinController")
public class JoinController {

    private final JoinService joinService;

    @Autowired
    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping(value = "/joinProc", consumes = "application/x-www-form-urlencoded")
    public RedirectView joinProc(@RequestParam("username") String userName,
                                 @RequestParam("password") String password,
                                 @RequestParam("email") String email,
                                 @RequestParam("address") String address){
        Join join = new Join();
        join.setUserName(userName);
        join.setPassword(password);
        join.setEmail(email);  // 이메일 설정
        join.setAddress(address);  // 주소 설정
        joinService.joinProcess(join);
        return new RedirectView("/");
    }
}
