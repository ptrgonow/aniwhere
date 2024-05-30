package com.aniwhere.domain.usermng.register.controller;

import com.aniwhere.domain.usermng.register.domain.Join;
import com.aniwhere.domain.usermng.register.service.JoinService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller("JoinController")
public class JoinController {

    private final JoinService joinService;

    @Autowired
    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @GetMapping("/join")
    public String joinPage() {
        return "popup/join";
    }

    @PostMapping(value = "/joinProc", consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public String joinProc(@RequestParam("username") String userName, @RequestParam("password") String password) {
        Join join = new Join();
        join.setUserName(userName);
        join.setPassword(password);
        joinService.joinProcess(join);
        return "redirect:/";
    }
}
