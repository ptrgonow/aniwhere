package com.aniwhere.domain.main;

import com.aniwhere.domain.user.join.service.HomeService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;


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
        return "index"; // index.html 템플릿으로 이동
    }



    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
