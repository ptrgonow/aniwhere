package com.aniwhere.domain.main;

import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@AllArgsConstructor
public class MainController {

    private final HomeService homeService;

    @GetMapping("/")
    public String home(Model model) {
        String userName = homeService.getAuthenticatedUserName();
        if (userName != null) {
            model.addAttribute("name", userName);
        }
        return "index";
    }
}
