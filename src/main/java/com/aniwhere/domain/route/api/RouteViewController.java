package com.aniwhere.domain.route.api;

import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/route")
@RequiredArgsConstructor
public class RouteViewController {

    private final HomeService homeService;

    @GetMapping("/cre")
    public String createRoute() {
        return "aniwhere/route/create";
    }


}
