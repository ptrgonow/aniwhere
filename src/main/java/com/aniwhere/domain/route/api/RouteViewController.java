package com.aniwhere.domain.route.api;

import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/route")
@RequiredArgsConstructor
public class RouteViewController {

    private final HomeService homeService;

    @GetMapping("/cre")
    public String createRoute(Model model) {

        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "aniwhere/route/create";
    }


}
