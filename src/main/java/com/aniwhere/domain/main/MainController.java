package com.aniwhere.domain.main;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller("mainController")
public class MainController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
