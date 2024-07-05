package com.aniwhere.domain.user.join.controller;

import com.aniwhere.domain.user.join.domain.Join;
import com.aniwhere.domain.user.join.service.JoinService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/joinProc")
    public String joinProc(@RequestBody Join join) {
        joinService.joinProcess(join);
        return "success";
    }
}
