package com.aniwhere.domain.route.api;

import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.route.service.RouteService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/route")
@RequiredArgsConstructor
public class RouteViewController {

    private final HomeService homeService;
    private final RouteService routeService;

    @GetMapping("/cre")
    public String createRoute(Model model) {

        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "aniwhere/route/create";
    }

    @GetMapping("/list")
    public String getRoutes(@RequestParam(defaultValue = "3") int limit, @RequestParam(defaultValue = "0") int offset, Model model) {

        List<RouteDTO> routes = routeService.getRoutes(limit, offset);
        int totalRoutes = routeService.countRoutes();
        int currentPage = offset / limit + 1;
        int totalPages = (int) Math.ceil((double) totalRoutes / limit);

        model.addAttribute("routes", routes);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("limit", limit);
        model.addAttribute("offset", offset);
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());

        return "aniwhere/route/list";
    }


}
