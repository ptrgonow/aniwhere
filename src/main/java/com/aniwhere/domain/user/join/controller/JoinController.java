package com.aniwhere.domain.user.join.controller;

import com.aniwhere.domain.user.join.domain.Join;
import com.aniwhere.domain.user.join.service.JoinService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@AllArgsConstructor
public class JoinController {

    private final JoinService joinService;


    @PostMapping("/joinProc")
    public RedirectView joinProc(Join join) {
        joinService.joinProcess(join);
        return new RedirectView("/");
    }
}
