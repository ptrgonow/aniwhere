package com.aniwhere.domain.route.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("routeViewController")
@RequestMapping("/route")
public class ViewController {

    @GetMapping("/cor")
    public String course() {
        return "aniwhere/route/course-create";
    }

    @GetMapping("/cor-list")
    public String courList() {
        return "aniwhere/route/course-list";
    }

    @GetMapping("/runner")
    public String runnerList() {
        return "aniwhere/runner/runner-list";
    }
}
