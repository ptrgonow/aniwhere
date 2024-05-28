package com.aniwhere.domain.usermng.api;

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
}
